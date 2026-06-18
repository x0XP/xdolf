package com.darkcart.xdolf.util;

import java.util.ArrayList;

import net.minecraft.util.StringUtils;

public class FriendManager
{
	public static ArrayList<Friend> friendsList = new ArrayList<Friend>();
	
	public void addFriend(Friend friend)
	{
		friendsList.add(friend);
	}

	public void addFriend(String name)
	{
		addFriend(new Friend(name));
	}

	public void removeFriend(Friend friend)
	{
		friendsList.remove(friend);
	}

	public void removeFriend(String name)
	{
		removeFriend(getFriend(name));
	}

	public Friend getFriend(String name)
	{
		for(Friend friend: friendsList)
		{
			if(friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name)))
			{
				return friend;
			}
		}

		return null;
	}

	public boolean isFriend(Friend friend)
	{
		return friendsList.contains(friend);
	}

	public boolean isFriend(String name)
	{
		return getFriend(name) != null;
	}
}
