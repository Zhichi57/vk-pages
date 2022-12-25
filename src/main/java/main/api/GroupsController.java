package main.api;

import main.api.Data.GroupConverter;
import main.api.Service.GroupService;
import main.api.Service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class GroupsController {

    private final GroupService groupService;
    private final QueryService queryService;

    private final GroupConverter groupConverter;

    @Autowired
    private VkConfig vkConfig;

    public GroupsController(GroupService groupService, QueryService queryService, GroupConverter groupConverter) {
        this.groupService = groupService;
        this.queryService = queryService;
        this.groupConverter = groupConverter;
    }


    @GetMapping("/groups/friends/{user_id}")
    ResponseEntity<Map<String,Object>> friends_groups(@PathVariable("user_id") Integer user_id, @RequestParam("query") String query) throws JSONException, InterruptedException {

        GroupsAPI groupsAPI = new GroupsAPI(vkConfig, user_id, query, groupService, queryService, groupConverter);
        return groupsAPI.getFriendsGroups();
    }

    @GetMapping("/groups/{user_id}")
    ResponseEntity<Map<String,Object>> groups(@PathVariable("user_id") Integer user_id, @RequestParam("query") String query) throws JSONException, InterruptedException {

        GroupsAPI groupsAPI = new GroupsAPI(vkConfig, user_id, query, groupService, queryService, groupConverter);
        return groupsAPI.getUserGroups();
    }

    @GetMapping("/groups/list/{user_id}")
    ResponseEntity<Map<String,Object>> groups_list(@PathVariable("user_id") Integer user_id,
                                                   @RequestParam("page") int page,
                                                   @RequestParam("size") int size)
            throws JSONException, InterruptedException {

        GroupsAPI groupsAPI = new GroupsAPI(vkConfig, user_id, groupService, queryService, groupConverter);
        return groupsAPI.getGroupList(page, size);
    }


}
