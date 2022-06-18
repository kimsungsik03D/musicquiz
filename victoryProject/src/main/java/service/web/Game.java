package service.web;

import java.util.HashMap;
import java.util.List;

public interface Game {

	int getClearTime();

	int getQuestionCount();

	int getToYear();

	void setSongList(List<Integer> songList);

	int getFromYear();

	HashMap getSongList();

}
