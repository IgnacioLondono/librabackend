# Diagrama de Arquitectura - Aplicación Android

## Arquitectura General

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  Activities  │  │  Fragments   │  │   Adapters   │     │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘     │
│         │                 │                  │              │
│  ┌──────▼─────────────────▼──────────────────▼───────┐   │
│  │              ViewModels (MVVM)                      │   │
│  └──────────────────────┬──────────────────────────────┘   │
└─────────────────────────┼──────────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────────┐
│                    DOMAIN LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │  Use Cases   │  │   Models     │  │  Interfaces  │    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└─────────────────────────┬──────────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────────┐
│                     DATA LAYER                             │
│  ┌────────────────────────────────────────────────────┐   │
│  │              Repositories                          │   │
│  └──────┬──────────────────────────────┬─────────────┘   │
│         │                              │                  │
│  ┌──────▼──────────┐        ┌──────────▼──────────┐      │
│  │  Remote Data    │        │   Local Data       │      │
│  │  (APIs)         │        │   (Room/SharedPref) │      │
│  └─────────────────┘        └─────────────────────┘      │
└───────────────────────────────────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────────┐
│              EXTERNAL SERVICES                             │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐    │
│  │  User    │ │  Book    │ │  Loan    │ │  Notif.  │    │
│  │  Service │ │  Service │ │  Service │ │  Service │    │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘    │
│  ┌──────────┐                                             │
│  │ Reports  │                                             │
│  │ Service  │                                             │
│  └──────────┘                                             │
└───────────────────────────────────────────────────────────┘
```

## Flujo de Datos

### Flujo de Autenticación

```
LoginFragment
    │
    ▼
LoginViewModel
    │
    ▼
LoginUseCase
    │
    ▼
UserRepository
    │
    ▼
UserApi (Retrofit)
    │
    ▼
User Management Service
    │
    ▼
Response (Token + User)
    │
    ▼
Save to Local Storage
    │
    ▼
Navigate to Home
```

### Flujo de Préstamo

```
BookListFragment
    │
    ▼
Select Book
    │
    ▼
BookDetailViewModel
    │
    ▼
CheckAvailabilityUseCase
    │
    ▼
BookRepository → BookApi
    │
    ▼
Available? → CreateLoanUseCase
    │
    ▼
LoanRepository → LoanApi
    │
    ▼
Loan Created → Update UI
```

## Componentes Principales

### Presentation Layer
- **Activities:** MainActivity, LoginActivity
- **Fragments:** LoginFragment, BookListFragment, LoanListFragment
- **ViewModels:** LoginViewModel, BookViewModel, LoanViewModel
- **Adapters:** BookAdapter, LoanAdapter

### Domain Layer
- **Use Cases:** LoginUseCase, CreateLoanUseCase, GetBooksUseCase
- **Models:** User, Book, Loan, Notification
- **Interfaces:** UserRepository, BookRepository, LoanRepository

### Data Layer
- **Repositories:** UserRepositoryImpl, BookRepositoryImpl
- **APIs:** UserApi, BookApi, LoanApi (Retrofit interfaces)
- **DTOs:** UserDto, BookDto, LoanDto
- **Local:** Room Database, SharedPreferences

### Dependency Injection
- **Hilt Modules:** RemoteModule, LocalModule, AppModule
- **Provides:** Retrofit instances, Repository instances



