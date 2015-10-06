package control;

import java.io.Serializable;

import game.GameLogic;

public interface NetworkAction extends Serializable{
	boolean applyAction(GameLogic logic);
}
