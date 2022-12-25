package main.api.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "queries", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Query {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "query")
    private String query;

    @Temporal(TemporalType.DATE)
    private Date query_date;

    @Temporal(TemporalType.TIME)
    private Date query_time;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "queries_groups",
            joinColumns = @JoinColumn(name = "query_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> group_list;
}
