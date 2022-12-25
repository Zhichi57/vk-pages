package main.api.Data;
import lombok.Data;

@Data
public class GroupDto {
    private int id;
    private String name;
    private String screenName;
    private int isClosed;
    private String type;
}
