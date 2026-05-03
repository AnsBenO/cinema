package ntt.beca.films.shared.base;

public abstract class BaseDto {

      @Override
      public boolean equals(Object obj) {
            return obj == this || (obj != null && obj.getClass() == this.getClass());
      }

      @Override
      public int hashCode() {
            return super.hashCode();
      }

}
