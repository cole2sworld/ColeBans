package com.cole2sworld.ColeBans.framework;

import org.bukkit.event.block.Action;

public enum SimpleAction {
	LEFT_CLICK,
	RIGHT_CLICK,
	PRESSURE_PLATE,
	UNKNOWN;
	public static SimpleAction forAction(Action complex) {
		if (complex == Action.RIGHT_CLICK_AIR || complex == Action.RIGHT_CLICK_BLOCK) return RIGHT_CLICK;
		if (complex == Action.LEFT_CLICK_AIR || complex == Action.LEFT_CLICK_BLOCK) return LEFT_CLICK;
		if (complex == Action.PHYSICAL) return PRESSURE_PLATE;
		return UNKNOWN;
	}
}
