package pl.dashclever.commons.hibernate;

public abstract class ConstHashCodeEntity<T> {

    protected abstract T getIdentifierValue();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConstHashCodeEntity<T> other = (ConstHashCodeEntity<T>) obj;
        return this.getIdentifierValue() != null && this.getIdentifierValue().equals(other.getIdentifierValue());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
