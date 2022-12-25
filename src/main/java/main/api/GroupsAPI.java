package main.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import main.api.Data.*;
import main.api.Service.GroupService;
import main.api.Service.QueryService;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
public class GroupsAPI {
    private Integer user_id;
    private String query;
    public static final String base_url = "https://api.vk.com/method";
    private VkConfig vkConfig;

    private final GroupService groupService;

    private final QueryService queryService;

    private final GroupConverter groupConverter;

    public GroupsAPI(VkConfig vkConfig, Integer user_id, String query, GroupService groupService, QueryService queryService, GroupConverter groupConverter) {
        this.vkConfig = vkConfig;
        this.user_id = user_id;
        this.query = query;
        this.groupService = groupService;
        this.queryService = queryService;
        this.groupConverter = groupConverter;
    }

    public GroupsAPI(VkConfig vkConfig, Integer user_id, GroupService groupService, QueryService queryService, GroupConverter groupConverter) {
        this.vkConfig = vkConfig;
        this.user_id = user_id;
        this.groupService = groupService;
        this.queryService = queryService;
        this.groupConverter = groupConverter;
    }


    public ResponseEntity<Map<String,Object>> getFriendsGroups() throws JSONException, InterruptedException {
        WebClient webClient = WebClient.create(base_url);
        String uri = String.format("/friends.get?user_id=%s&access_token=%s&v=%s", this.user_id, this.vkConfig.getToken(), this.vkConfig.getVersion());
        String json_response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONObject obj = new JSONObject(json_response);
        JSONArray friends_list = obj.getJSONObject("response").getJSONArray("items");
        List<String> friends_group = new ArrayList<String>();
        for (int i = 0; i <= friends_list.length(); i++)
        {
            friends_group.addAll(get_user_group(friends_list.getString(i)));
            TimeUnit.SECONDS.sleep(1);
        }

        Map<String, Object> message = new HashMap<String, Object>();
        List<GroupDto> groupDtos = find_group(this.query, friends_group).stream().map(groupConverter::convertToResponse).toList();
        message.put("response", groupDtos);
        return new ResponseEntity<Map<String,Object>>(message, HttpStatus.OK);
    }

    public ResponseEntity<Map<String,Object>> getUserGroups() throws JSONException {
        List<String> user_groups = get_user_group(String.valueOf(this.user_id));
        Map<String, Object> message = new HashMap<String, Object>();
        List<Group> found_groups = find_group(this.query, user_groups);
        List<GroupDto> groupDtos = found_groups.stream().map(groupConverter::convertToResponse).toList();

        message.put("response", groupDtos);

        Date now = new Date();
        Query query = new Query();
        query.setQuery(this.query);
        query.setQuery_date(now);
        query.setQuery_time(now);
        query.setGroup_list(found_groups);
        Integer query_id = queryService.addQuery(query);

        return new ResponseEntity<Map<String,Object>>(message, HttpStatus.OK);
    }

    public List<String> get_user_group(String user_id) throws JSONException {
        WebClient webClient = WebClient.create(base_url);
        String uri = String.format("/groups.get?user_id=%s&access_token=%s&v=%s", user_id, this.vkConfig.getToken(), this.vkConfig.getVersion());
        String json_response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONObject obj = new JSONObject(json_response);
        List<String> list = new ArrayList<String>();
        JSONArray friends_list = obj.getJSONObject("response").getJSONArray("items");
        for(int i = 0; i < friends_list.length(); i++){
            list.add(friends_list.getString(i));
        }
        return list;
    }

    public List<Group> find_group(String query, List<String> groups_id) throws JSONException {
        WebClient webClient = WebClient.create(base_url);
        String uri = String.format("/groups.search?q=%s&access_token=%s&v=%s", query, this.vkConfig.getToken(), this.vkConfig.getVersion());
        String json_response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONObject obj = new JSONObject(json_response);
        JSONArray groups = obj.getJSONObject("response").getJSONArray("items");
        List<Group> group_list = new ArrayList<Group>();
        Gson gson = new GsonBuilder().create();
        for(int i = 0; i < groups.length(); i++){
            if (groups_id.contains(groups.getJSONObject(i).getString("id"))) {
                Group tmp_grp = gson.fromJson(groups.getJSONObject(i).toString(), Group.class);
                tmp_grp.setUserid(this.user_id);
                group_list.add(tmp_grp);
            }
        }
        return group_list;
    }

    public ResponseEntity<Map<String,Object>> getGroupList(int page, int size){
        List<Group> groupList = groupService.listGroups(this.user_id, page, size);
        List<GroupDto> groupDtos = groupList.stream().map(groupConverter::convertToResponse).toList();
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("response", groupDtos);

        return new ResponseEntity<Map<String,Object>>(message, HttpStatus.FOUND);
    }

}


