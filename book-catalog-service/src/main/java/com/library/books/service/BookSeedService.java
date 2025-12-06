package com.library.books.service;

import com.library.books.dto.BookCreateDTO;
import com.library.books.dto.SeedResponseDTO;
import com.library.books.model.Book;
import com.library.books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para cargar datos iniciales (seed) de libros
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookSeedService {

    private final BookRepository bookRepository;

    /**
     * Cargar los 34 libros precargados en la base de datos
     * @param forceReload Si es true, elimina los libros existentes antes de cargar
     */
    @Transactional
    public SeedResponseDTO loadInitialBooks(boolean forceReload) {
        log.info("Iniciando carga de libros precargados... (forceReload: {})", forceReload);

        // Si se fuerza la recarga, eliminar todos los libros existentes
        if (forceReload) {
            long countBefore = bookRepository.count();
            bookRepository.deleteAll();
            log.info("Eliminados {} libros existentes para recarga forzada", countBefore);
        }

        List<BookCreateDTO> initialBooks = getInitialBooks();
        int totalProcessed = initialBooks.size();
        int inserted = 0;
        int alreadyExists = 0;
        int errors = 0;

        for (BookCreateDTO bookDTO : initialBooks) {
            try {
                // Verificar si existe por ISBN
                boolean exists = false;
                if (bookDTO.getIsbn() != null && !bookDTO.getIsbn().trim().isEmpty()) {
                    exists = bookRepository.findByIsbn(bookDTO.getIsbn().trim()).isPresent();
                }

                // Si no existe por ISBN, verificar por título + autor
                if (!exists) {
                    exists = bookRepository.findAll().stream()
                            .anyMatch(b -> b.getTitle().equalsIgnoreCase(bookDTO.getTitle().trim()) &&
                                    b.getAuthor().equalsIgnoreCase(bookDTO.getAuthor().trim()));
                }

                if (exists) {
                    alreadyExists++;
                    log.debug("Libro ya existe: {} - {}", bookDTO.getTitle(), bookDTO.getAuthor());
                } else {
                    // Crear el libro
                    int totalCopies = bookDTO.getTotalCopies() != null ? bookDTO.getTotalCopies() : 1;
                    // Obtener availableCopies específico para este libro (algunos tienen valores diferentes)
                    int availableCopies = getAvailableCopiesForBook(bookDTO.getTitle(), totalCopies);
                    
                    Book book = Book.builder()
                            .title(bookDTO.getTitle().trim())
                            .author(bookDTO.getAuthor().trim())
                            .isbn(bookDTO.getIsbn() != null ? bookDTO.getIsbn().trim() : null)
                            .category(bookDTO.getCategory() != null ? bookDTO.getCategory().trim() : null)
                            .publisher(bookDTO.getPublisher() != null ? bookDTO.getPublisher().trim() : null)
                            .year(bookDTO.getYear())
                            .description(bookDTO.getDescription() != null ? bookDTO.getDescription().trim() : null)
                            .coverUrl(bookDTO.getCoverUrl() != null && !bookDTO.getCoverUrl().trim().isEmpty() 
                                    ? bookDTO.getCoverUrl().trim() : null)
                            .totalCopies(totalCopies)
                            .availableCopies(availableCopies)
                            .price(bookDTO.getPrice())
                            .featured(bookDTO.getFeatured() != null ? bookDTO.getFeatured() : false)
                            .build();

                    bookRepository.save(book);
                    inserted++;
                    log.debug("Libro insertado: {} - {} (Total: {}, Disponibles: {})", 
                            book.getTitle(), book.getAuthor(), totalCopies, availableCopies);
                }
            } catch (Exception e) {
                errors++;
                log.error("Error insertando libro {} - {}: {}", bookDTO.getTitle(), bookDTO.getAuthor(), e.getMessage());
            }
        }

        String message = String.format("Se insertaron %d libros nuevos. %d ya existían. %d errores.",
                inserted, alreadyExists, errors);

        log.info("Carga de libros completada: {}", message);

        return SeedResponseDTO.builder()
                .totalProcessed(totalProcessed)
                .inserted(inserted)
                .alreadyExists(alreadyExists)
                .errors(errors)
                .message(message)
                .build();
    }

    /**
     * Obtener la lista de 34 libros precargados
     */
    private List<BookCreateDTO> getInitialBooks() {
        List<BookCreateDTO> books = new ArrayList<>();

        books.add(BookCreateDTO.builder()
                .title("1984")
                .author("George Orwell")
                .isbn("9788497593793")
                .category("Ciencia")
                .publisher("Debolsillo")
                .year(1949)
                .description("Una distopía clásica que explora el totalitarismo y la vigilancia estatal.")
                .coverUrl("https://books.google.cl/books/content?id=yxv1LK5gyV4C&printsec=frontcover&img=1&zoom=1&imgtk=AFLRE709ApWTwmDGcaEhgVM4kCSB3tbQ8lPS_YcHkQt2rJfr8baeRKvcz0sfK_ZbHCwnvHVI5f2NzbPQqemPhT78pIJk1UeG3IV30GdzMvNYquT1VglrvYIcd9CNGO51NkgJsw6gM1kF")
                .totalCopies(5)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("El Principito")
                .author("Antoine de Saint-Exupéry")
                .isbn("9788498381497")
                .category("Literatura")
                .publisher("Salamandra")
                .year(1943)
                .description("Un cuento filosófico sobre la amistad, el amor y la pérdida de la inocencia.")
                .coverUrl("https://m.media-amazon.com/images/I/713nEf55PkL._SL1500_.jpg")
                .totalCopies(8)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Orgullo y prejuicio")
                .author("Jane Austen")
                .isbn("9788491051534")
                .category("Literatura")
                .publisher("Penguin Clásicos")
                .year(1813)
                .description("Una novela romántica que critica la sociedad inglesa del siglo XIX.")
                .coverUrl("https://tse2.mm.bing.net/th/id/OIP.rVCEEI0KvromX4U9hDdnVwHaLJ?rs=1&pid=ImgDetMain&o=7&rm=3")
                .totalCopies(6)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Moby-Dick")
                .author("Herman Melville")
                .isbn("9788491051541")
                .category("Literatura")
                .publisher("Alma")
                .year(1851)
                .description("La épica historia de la obsesión del capitán Ahab con la ballena blanca.")
                .coverUrl("https://www.planetalibro.net/biblioteca/m/e/melville/melville-herman-moby-dick/melville-herman-moby-dick.jpg")
                .totalCopies(4)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Cien años de soledad")
                .author("Gabriel García Márquez")
                .isbn("9788437604947")
                .category("Literatura")
                .publisher("Diana")
                .year(1967)
                .description("La saga de la familia Buendía en el mítico pueblo de Macondo.")
                .coverUrl("https://th.bing.com/th/id/R.96daf80eb401e6eca4c96e5c6a2ab7ac?rik=HpeztcqjJWyrzw&pid=ImgRaw&r=0")
                .totalCopies(7)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Dune")
                .author("Frank Herbert")
                .isbn("9788497593794")
                .category("Ciencia Ficción")
                .publisher("Debolsillo")
                .year(1965)
                .description("Una épica de ciencia ficción ambientada en el desértico planeta Arrakis.")
                .coverUrl("https://1.bp.blogspot.com/-xO-f2oZ5ZJ0/Xux2y28OJZI/AAAAAAAAZMA/AsHZHkdR8OIYK6mHcACR-GcU3qkyjRvPACNcBGAsYHQ/s1600/dune.jpg")
                .totalCopies(10)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("El Hobbit")
                .author("J.R.R. Tolkien")
                .isbn("9788445071408")
                .category("Fantasía")
                .publisher("Minotauro")
                .year(1937)
                .description("La aventura de Bilbo Bolsón en su viaje inesperado hacia la Montaña Solitaria.")
                .coverUrl("https://www.raccoongames.es/img/productos/2022/04/02/portada_el-hobbit-ne_j-r-r-tolkien_202202140958.jpeg")
                .totalCopies(12)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Harry Potter y la piedra filosofal")
                .author("J.K. Rowling")
                .isbn("9788478884452")
                .category("Fantasía")
                .publisher("Salamandra")
                .year(1997)
                .description("El primer libro de la saga del joven mago Harry Potter.")
                .coverUrl("https://i0.wp.com/www.epubgratis.org/wp-content/uploads/2012/04/Harry-Potter-y-la-Piedra-Filosofal-J.K.-Rowling-portada.jpg?fit=683%2C1024&ssl=1")
                .totalCopies(15)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Neuromante")
                .author("William Gibson")
                .isbn("9788445071415")
                .category("Ciencia Ficción")
                .publisher("Minotauro")
                .year(1984)
                .description("La novela fundacional del cyberpunk sobre hackers y inteligencia artificial.")
                .coverUrl("https://tse1.mm.bing.net/th/id/OIP.EWv-kvt7VepbXpJWrVX2jwHaLQ?rs=1&pid=ImgDetMain&o=7&rm=3")
                .totalCopies(3)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Fundación")
                .author("Isaac Asimov")
                .isbn("9788497593795")
                .category("Ciencia Ficción")
                .publisher("Debolsillo")
                .year(1951)
                .description("El inicio de la saga sobre la caída y reconstrucción del Imperio Galáctico.")
                .coverUrl("https://1.bp.blogspot.com/-VNj464HCl00/WW5b5Oqhz2I/AAAAAAAAGHE/tQuUsLgj3aA7oL0gz_60vYN9yYgBPIlaACKgBGAs/s1600/fundacion-libro-isaac-asimov.JPG")
                .totalCopies(9)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Bajo la misma estrella")
                .author("John Green")
                .isbn("9788415594024")
                .category("Juvenil")
                .publisher("Nube de Tinta")
                .year(2012)
                .description("Una historia conmovedora sobre dos adolescentes que luchan contra el cáncer.")
                .coverUrl("https://th.bing.com/th/id/R.e134c57e2b93d7ac6cd12d355241fbd0?rik=utPtyzXJG3v7Kg&riu=http%3a%2f%2f3.bp.blogspot.com%2f-Dmyq00jXSz0%2fUlVaaMi6NwI%2fAAAAAAAAAq8%2f_J-CmEj4hYs%2fs1600%2fPortada%2bBajo%2bla%2bmisma%2bestrella.jpg&ehk=mDlhTmrwbB8Zt51I0DZ9tDk40XGsiMyRXdI%2fwNRUj0Y%3d&risl=&pid=ImgRaw&r=0")
                .totalCopies(11)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Romeo y Julieta")
                .author("William Shakespeare")
                .isbn("9788467033094")
                .category("Literatura")
                .publisher("Austral")
                .year(1597)
                .description("La trágica historia de amor de dos jóvenes de familias enemigas en Verona.")
                .coverUrl("https://th.bing.com/th/id/OIP.fIWSKNeoWmumrY9se0TFzAHaLu?w=198&h=314&c=7&r=0&o=7&pid=1.7&rm=3")
                .totalCopies(6)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Lo que el viento se llevó")
                .author("Margaret Mitchell")
                .isbn("9788497593796")
                .category("Historia")
                .publisher("Ediciones B")
                .year(1936)
                .description("Un épico romance ambientado durante la Guerra Civil estadounidense.")
                .coverUrl("https://tse4.mm.bing.net/th/id/OIP.hZqqeJCVUVYLM7xvuNx79AHaLT?rs=1&pid=ImgDetMain&o=7&rm=3")
                .totalCopies(2)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Jane Eyre")
                .author("Charlotte Brontë")
                .isbn("9788491051558")
                .category("Literatura")
                .publisher("Alba")
                .year(1847)
                .description("La historia de una joven huérfana que encuentra el amor y la independencia.")
                .coverUrl("https://images.cdn3.buscalibre.com/fit-in/360x360/9b/4d/9b4d0c92dd7e99db4cfafad1cc33c5a8.jpg")
                .totalCopies(7)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Cometas en el cielo")
                .author("Khaled Hosseini")
                .isbn("9788498381503")
                .category("Historia")
                .publisher("Salamandra")
                .year(2003)
                .description("Una historia de amistad y redención en el Afganistán de los años 70.")
                .coverUrl("https://m.media-amazon.com/images/I/81qKWjnjayL._SL1500_.jpg")
                .totalCopies(8)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("El código Da Vinci")
                .author("Dan Brown")
                .isbn("9788408096315")
                .category("Misterio")
                .publisher("Planeta")
                .year(2003)
                .description("Un thriller que mezcla historia, arte y conspiraciones religiosas.")
                .coverUrl("https://tse2.mm.bing.net/th/id/OIP.sIkdDAiKEwynPBn-jjhVEQHaLH?rs=1&pid=ImgDetMain&o=7&rm=3")
                .totalCopies(13)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Asesinato en el Orient Express")
                .author("Agatha Christie")
                .isbn("9788497593797")
                .category("Misterio")
                .publisher("Molino")
                .year(1934)
                .description("El famoso detective Hércules Poirot resuelve un asesinato en un tren.")
                .coverUrl("https://tse2.mm.bing.net/th/id/OIP.qET_a8U2StF1NzsE8z-s1QAAAA?rs=1&pid=ImgDetMain&o=7&rm=3")
                .totalCopies(10)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("El silencio de los corderos")
                .author("Thomas Harris")
                .isbn("9788497593798")
                .category("Suspenso")
                .publisher("Debolsillo")
                .year(1988)
                .description("Un thriller psicológico sobre un asesino en serie y una joven agente del FBI.")
                .coverUrl("https://www.izicomics.com/wp-content/uploads/2020/03/descargar-libro-el-silencio-de-los-corderos-en-pdf-epub-mobi-o-leer-online.jpg")
                .totalCopies(6)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("It")
                .author("Stephen King")
                .isbn("9788497593799")
                .category("Terror")
                .publisher("Debolsillo")
                .year(1986)
                .description("Una novela de terror sobre un grupo de amigos que enfrenta a un monstruo.")
                .coverUrl("https://imagessl3.casadellibro.com/a/l/t0/93/9788497593793.jpg")
                .totalCopies(5)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Los hombres que no amaban a las mujeres")
                .author("Stieg Larsson")
                .isbn("9788498381510")
                .category("Misterio")
                .publisher("Destino")
                .year(2005)
                .description("El primer libro de la trilogía Millennium sobre crímenes y corrupción.")
                .coverUrl("https://tse1.mm.bing.net/th/id/OIP.mDYTAHVvbhrsaLIllZo39gHaLQ?rs=1&pid=ImgDetMain")
                .totalCopies(9)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Breve Historia del Tiempo")
                .author("Stephen Hawking")
                .isbn("9788408096322")
                .category("Ciencia")
                .publisher("Crítica")
                .year(1988)
                .description("Una exploración accesible de los conceptos fundamentales de la física moderna y el cosmos.")
                .coverUrl("")
                .totalCopies(8)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Don Quijote de la Mancha")
                .author("Miguel de Cervantes")
                .isbn("9788491051565")
                .category("Literatura")
                .publisher("Real Academia Española")
                .year(1605)
                .description("La obra cumbre de la literatura española sobre las aventuras del caballero andante.")
                .coverUrl("")
                .totalCopies(12)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Blade Runner")
                .author("Philip K. Dick")
                .isbn("9788445071422")
                .category("Ciencia Ficción")
                .publisher("Minotauro")
                .year(1968)
                .description("Una novela distópica sobre cazadores de androides en un futuro cercano.")
                .coverUrl("")
                .totalCopies(6)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Juego de Tronos")
                .author("George R.R. Martin")
                .isbn("9788496208647")
                .category("Fantasía")
                .publisher("Gigamesh")
                .year(1996)
                .description("El primer libro de la saga Canción de Hielo y Fuego sobre las casas nobles de Westeros.")
                .coverUrl("")
                .totalCopies(14)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Sapiens: De Animales a Dioses")
                .author("Yuval Noah Harari")
                .isbn("9788499926223")
                .category("Historia")
                .publisher("Debate")
                .year(2011)
                .description("Una breve historia de la humanidad desde la evolución hasta la actualidad.")
                .coverUrl("")
                .totalCopies(10)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Percy Jackson y el Ladrón del Rayo")
                .author("Rick Riordan")
                .isbn("9788498381527")
                .category("Juvenil")
                .publisher("Salamandra")
                .year(2005)
                .description("La primera aventura del joven semidiós Percy Jackson en el mundo de los dioses griegos.")
                .coverUrl("")
                .totalCopies(15)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("La Chica del Tren")
                .author("Paula Hawkins")
                .isbn("9788498381534")
                .category("Misterio")
                .publisher("Planeta")
                .year(2015)
                .description("Un thriller psicológico sobre una mujer que observa una pareja desde el tren cada día.")
                .coverUrl("")
                .totalCopies(7)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Gone Girl")
                .author("Gillian Flynn")
                .isbn("9788498381541")
                .category("Suspenso")
                .publisher("Planeta")
                .year(2012)
                .description("Un thriller sobre la desaparición de una mujer y las sospechas que recaen sobre su marido.")
                .coverUrl("")
                .totalCopies(9)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("El Exorcista")
                .author("William Peter Blatty")
                .isbn("9788497593805")
                .category("Terror")
                .publisher("Debolsillo")
                .year(1971)
                .description("Una novela de terror sobre el exorcismo de una niña poseída por fuerzas demoníacas.")
                .coverUrl("")
                .totalCopies(3)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("El Universo Elegante")
                .author("Brian Greene")
                .isbn("9780143116348")
                .category("Ciencia")
                .publisher("Vintage")
                .year(2003)
                .description("Una exploración fascinante de la teoría de cuerdas y las dimensiones ocultas del universo.")
                .coverUrl("")
                .totalCopies(8)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("Cumbres Borrascosas")
                .author("Emily Brontë")
                .isbn("9780141439556")
                .category("Literatura")
                .publisher("Penguin Classics")
                .year(2001)
                .description("Una novela gótica sobre el amor apasionado y la venganza en los páramos de Yorkshire.")
                .coverUrl("")
                .totalCopies(6)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("El Marciano")
                .author("Andy Weir")
                .isbn("9780804139021")
                .category("Ciencia Ficción")
                .publisher("Crown")
                .year(2014)
                .description("La historia de un astronauta que debe sobrevivir solo en Marte después de ser abandonado por su tripulación.")
                .coverUrl("")
                .totalCopies(10)
                .featured(true)
                .build());

        books.add(BookCreateDTO.builder()
                .title("El Nombre del Viento")
                .author("Patrick Rothfuss")
                .isbn("9780756404741")
                .category("Fantasía")
                .publisher("DAW Books")
                .year(2007)
                .description("El primer libro de la trilogía Crónica del Asesino de Reyes, sobre la vida del legendario Kvothe.")
                .coverUrl("")
                .totalCopies(12)
                .featured(false)
                .build());

        books.add(BookCreateDTO.builder()
                .title("El Diario de Ana Frank")
                .author("Ana Frank")
                .isbn("9780553296983")
                .category("Historia")
                .publisher("Bantam")
                .year(1993)
                .description("El conmovedor diario de una joven judía que documenta su vida en la clandestinidad durante el Holocausto.")
                .coverUrl("")
                .totalCopies(9)
                .featured(true)
                .build());

        return books;
    }

    /**
     * Obtener availableCopies específico para libros que tienen valores diferentes a totalCopies
     */
    private int getAvailableCopiesForBook(String title, int totalCopies) {
        // Libros con availableCopies diferentes según los datos proporcionados
        switch (title) {
            case "Cien años de soledad":
                return 6; // totalCopies: 7
            case "Neuromante":
                return 2; // totalCopies: 3
            case "Lo que el viento se llevó":
                return 0; // totalCopies: 2
            case "It":
                return 4; // totalCopies: 5
            case "Blade Runner":
                return 5; // totalCopies: 6
            case "La Chica del Tren":
                return 6; // totalCopies: 7
            case "El Exorcista":
                return 0; // totalCopies: 3
            case "El Marciano":
                return 9; // totalCopies: 10
            default:
                return totalCopies; // Por defecto, todos disponibles
        }
    }
}

