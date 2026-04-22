package ntt.beca.films.shared.security;

import java.util.Arrays;
import java.util.List;

public enum Role {
      CUSTOMER(Arrays.asList(
                  Permission.READ_ALL_FILMS,
                  Permission.SUBMIT_ORDER,
                  Permission.READ_ALL_ORDERS)),

      MODERATOR(Arrays.asList(
                  Permission.READ_ALL_FILMS,
                  Permission.CREATE_FILM,
                  Permission.UPDATE_FILM,
                  Permission.READ_ALL_ORDERS,
                  Permission.UPDATE_ORDER_STATUS)),

      ADMIN(Arrays.asList(
                  Permission.READ_ALL_USERS,
                  Permission.UPDATE_USER_ROLE,
                  Permission.READ_ALL_FILMS,
                  Permission.CREATE_FILM,
                  Permission.UPDATE_FILM,
                  Permission.DELETE_FILM,
                  Permission.READ_ALL_ORDERS,
                  Permission.UPDATE_ORDER_STATUS,
                  Permission.DELETE_ORDER));

      private List<Permission> permissions;

      Role(List<Permission> permissions) {
            this.permissions = permissions;
      }

      public List<Permission> getPermissions() {
            return permissions;
      }

}
