package control;

import java.io.Serializable;

import game.GameLogic;

public interface NetworkAction extends Serializable{
	void applyAction(GameLogic logic);
}
