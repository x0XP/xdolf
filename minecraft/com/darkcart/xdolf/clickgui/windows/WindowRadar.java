package com.darkcart.xdolf.clickgui.windows;

import java.util.List;
import java.util.stream.Collectors;

import com.darkcart.xdolf.Wrapper;
import com.darkcart.xdolf.clickgui.elements.XdolfTextWindow;
import com.darkcart.xdolf.util.Friend;

import net.minecraft.entity.player.EntityPlayer;

public class WindowRadar extends XdolfTextWindow {
	public WindowRadar() {
		super("Radar", 2, 92);
	}

	@Override
	protected String[] getText() {

		List<EntityPlayer> players = Wrapper.getWorld().playerEntities.stream()
				.filter(e -> e != Wrapper.getPlayer() && !e.isDead)
				.sorted((e1, e2) -> (int)(Wrapper.getPlayer().getDistanceToEntity(e1) - Wrapper.getPlayer().getDistanceToEntity(e2)))
				.collect(Collectors.toList());

		if (players.isEmpty())
		{
			return new String[] { "No players in range." };
		}

		String[] lines = new String[players.size()];
		int count = 0;

		for(EntityPlayer e : players)
		{
			int distance = (int)Wrapper.getPlayer().getDistanceToEntity(e);
			Friend friend = Wrapper.getFriends().getFriend(e.getName());

			StringBuilder text = new StringBuilder();
			if (friend == null)
			{
				text.append("\247c" + e.getName());
			}
			else
			{
				text.append("\247a" + friend.getName());
			}
			text.append("\247f: " + distance);

			lines[count++] = text.toString();
		}

		return lines;
	}

}
