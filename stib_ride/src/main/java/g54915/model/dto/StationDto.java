package g54915.model.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * Data transfer object of a station. The key of the station is an integer, its
 * id.
 */
public class StationDto extends Dto<Integer> {

    private String name;
    private Set<Integer> lines;

    /**
     * Creates a new instance of <code>StationDto</code>.
     *
     * @param id id of the station.
     * @param n  name of the station.
     */
    public StationDto(int id, String n) {
        super(id);
        this.name = n;
        this.lines = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Integer getKey() {
        return super.key;
    }

    public Set<Integer> getLines() {
        return lines;
    }

    public void setLines(Set<Integer> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getLinesToString() {
        StringBuilder possibleLines = new StringBuilder("[");
        for (Integer line : this.lines) {
            possibleLines.append(line).append(", ");
        }
        String lines = possibleLines.toString();
        if (possibleLines.length() > 1) {
            lines = lines.substring(0, lines.length() - 2);
        }
        return lines + "]";
    }
}
