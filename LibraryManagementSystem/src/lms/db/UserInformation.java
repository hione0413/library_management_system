package lms.db;

import java.util.ArrayList;

public class UserInformation {
	String[] ColumnName = {"member_id", "user_name", "user_id", "user_pw"};
	ArrayList<String[]> current_User = new ArrayList<String[]>();
	String[] user_data = new String[ColumnName.length];

	public UserInformation(int primary_id, String name, String id, String pw) {
		Integer pid = primary_id;
		
		user_data[0] = pid.toString();
		user_data[1] = name;
		user_data[2] = id;
		user_data[3] = pw;
	}
	public void addCurrentUser() {
		current_User.add(user_data);
	}
	public void delCurrentUser(int index) {
		current_User.remove(index);
	}
}
