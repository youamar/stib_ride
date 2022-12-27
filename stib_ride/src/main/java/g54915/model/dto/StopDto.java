package g54915.model.dto;

import java.util.Objects;

import javafx.util.Pair;

/**
 * Data transfer object of a stop. The key of the stop is two integers, its line
 * and station.
 */
public class StopDto extends Dto<Pair<Integer, Integer>> {
    private final StationDto station;
    private final int line;
    private final int order;

    /**
     * Creates a new instance of <code>StopDto</code>.
     *
     * @param line    line of the stop.
     * @param station station of the stop.
     * @param order   order number of the stop (lets us know the position of the station in the line).
     */
    public StopDto(int line, StationDto station, int order) {
        super(new Pair<>(station.getKey(), line));
        this.station = station;
        this.line = line;
        this.order = order;
    }

    public StationDto getStation() {
        return station;
    }

    public int getLine() {
        return line;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StopDto stopDto = (StopDto) o;
        return line == stopDto.line && order == stopDto.order && Objects.equals(station, stopDto.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), station, line, order);
    }
}
