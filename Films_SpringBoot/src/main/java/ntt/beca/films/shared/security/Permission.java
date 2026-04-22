package ntt.beca.films.shared.security;

public enum Permission {

      // User Management
      READ_ALL_USERS,
      UPDATE_USER_ROLE,

      // Film Management
      READ_ALL_FILMS,
      CREATE_FILM,
      UPDATE_FILM,
      DELETE_FILM,

      // Order Management
      SUBMIT_ORDER,
      READ_ALL_ORDERS,
      UPDATE_ORDER_STATUS,
      DELETE_ORDER;

}
