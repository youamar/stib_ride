package g54915.model.dto;

import java.util.Objects;

public class FavoriteDto extends Dto<String> {
    private final StationDto source;
    private final StationDto destination;

    public FavoriteDto(String key, StationDto source, StationDto destination) {
        super(key);
        this.source = source;
        this.destination = destination;
    }

    public String getKey() {
        return super.key;
    }

    public StationDto getSource() {
        return source;
    }

    public StationDto getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FavoriteDto that = (FavoriteDto) o;
        return Objects.equals(source, that.source) && Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), source, destination);
    }
}