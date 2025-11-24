package com.library.api

object ApiConfig {
    // URLs base de los microservicios
    const val BASE_URL_USER = "http://10.0.2.2:8081" // 10.0.2.2 es localhost en emulador Android
    const val BASE_URL_BOOK = "http://10.0.2.2:8082"
    const val BASE_URL_LOAN = "http://10.0.2.2:8083"
    const val BASE_URL_REPORTS = "http://10.0.2.2:8084"
    const val BASE_URL_NOTIFICATIONS = "http://10.0.2.2:8085"
    
    // Para dispositivo físico, usa la IP de tu PC:
    // const val BASE_URL_USER = "http://192.168.1.X:8081"
    
    // Endpoints User Management
    object UserEndpoints {
        const val REGISTER = "/api/users/register"
        const val LOGIN = "/api/users/login"
        const val LOGOUT = "/api/users/logout"
        const val GET_USER = "/api/users/{userId}"
        const val UPDATE_USER = "/api/users/{userId}"
        const val VALIDATE_TOKEN = "/api/users/validate-token"
    }
    
    // Endpoints Book Catalog
    object BookEndpoints {
        const val CREATE_BOOK = "/api/books"
        const val GET_BOOK = "/api/books/{bookId}"
        const val GET_ALL_BOOKS = "/api/books"
        const val SEARCH_BOOKS = "/api/books/search"
        const val CHECK_AVAILABILITY = "/api/books/{bookId}/availability"
        const val GET_FEATURED = "/api/books/featured"
        const val GET_BY_CATEGORY = "/api/books/category/{category}"
    }
    
    // Endpoints Loan Management
    object LoanEndpoints {
        const val CREATE_LOAN = "/api/loans"
        const val GET_LOAN = "/api/loans/{loanId}"
        const val GET_USER_LOANS = "/api/loans/user/{userId}"
        const val GET_ACTIVE_LOANS = "/api/loans/user/{userId}/active"
        const val RETURN_LOAN = "/api/loans/{loanId}/return"
        const val EXTEND_LOAN = "/api/loans/{loanId}/extend"
        const val CANCEL_LOAN = "/api/loans/{loanId}/cancel"
        const val GET_OVERDUE = "/api/loans/overdue"
        const val VALIDATE_LOAN = "/api/loans/validate"
    }
    
    // Endpoints Notifications
    object NotificationEndpoints {
        const val GET_USER_NOTIFICATIONS = "/api/notifications/user/{userId}"
        const val GET_UNREAD_COUNT = "/api/notifications/user/{userId}/unread-count"
        const val MARK_AS_READ = "/api/notifications/{notificationId}/read"
        const val MARK_ALL_READ = "/api/notifications/user/{userId}/read-all"
    }
    
    // Endpoints Reports
    object ReportEndpoints {
        const val GET_DASHBOARD = "/api/reports/dashboard"
    }
}





