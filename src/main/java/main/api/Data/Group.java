package main.api.Data;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer dbId;

    @Column(name = "group_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "screen_name")
    @SerializedName(value="screen_name")
    private String screenName;

    @Column(name = "is_closed")
    @SerializedName(value="is_closed")
    private int isClosed;

    @Column(name = "type")
    private String type;

    @Column(name = "user_id")
    private int userid;

    @ManyToMany(mappedBy = "group_list")
    private Set<Query> queries;

}
