package com.asgard.game.views;

import com.asgard.game.models.Point;

public interface GameEventListener {

	public void blockClicked(BlockView block, Point direction);
}
