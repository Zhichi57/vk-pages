package main.api.Service;

import main.api.Data.Group;;
import java.util.List;

public interface GroupService {
    boolean saveGroup(Group group);

    List<Group> listGroups(int user_id, int page, int size);
}
