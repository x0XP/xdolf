package com.darkcart.xdolf.commands;

import java.util.Random;

import com.darkcart.xdolf.Wrapper;

public class CmdPraiseOre extends Command {

	String[] praises = { "Don't smoke pot around kids. I know from experience.",
			"I didn't beat my wife. She beat herself.", "Ignored.", "Bigotry is bad.",
			"please stop slandering me.. I don't approve of domestic violence.. and I've never hit a women. please send your unhealthy obsession to someone else",
			"The real question is why all the 2b2t edgy kids just can't let go and get over me and move on..",
			"I have never beat my wife, nor do I condone domestic abuse, which is what I say every time this slander created by popbob many years ago comes up.",
			"Lets get this clear.. You spend how many hours making this video ranting about me, and I'm the one raging 24/7. What a joke",
			"Stop being so mad I've got you ignored in-game. So, your biggest thing against me is that I'm a so called sjw, and that I stand up against bigotry and racism?",
			"I've never created a lag machine, anyone who based with me on 2b2t could confirm, there are 30+. As for my useless redstone, just because you don't understand it, doesn't make it useless.",
			"this seriously borders on stalking and harrassment.. Get over me and obsess about someone else. Not to mention this grif was really really poor job.",
			"comments like that aren't going to get me to.. try something like all the memes about oremonger are false",
			"Everyone at this base who built anything has been playing MC for years and has dedicated many hours to crafting their skills. My motto within MC has always been build big or go home.",
			"please stop slandering me.. I don't approve of domestic violence.. and I've never hit a women. please send your unhealthy obsession to someone else",
			"I\'m going to ask nicely once... remove \"\"I\'m going to Ore\'s house for Christmas to smoke weed and fuck his wife, it's the best present he could ask for\" -Kinorana 19/12/2016\". I\'ve had it with your cyber stalking and harrasment. Please end this now.",
			"I take this cyber stalking / harassment very seriously.. many of their \"crew\" of edgies have made threats against me and my family. Completely unacceptable",
			"threatening to attack my wife or kids is no joke.",
			"I take threats to my family very seriously and this person masteroftheconsoles aka itristan and his friends have been cyber stalking me, attempting to dox me, threatening me and my family for over two years. These teenagers are obsessed with me and it seems a few of them are mentally unstable as well. I have no idea who you are, but you would do best to stay as far away from this as possible." };

	public CmdPraiseOre() {
		super("praiseore");
	}

	@Override
	public void runCommand(String s, String[] args) {
		try {
			Wrapper.getPlayer().sendChatMessage(praises[new Random().nextInt(praises.length)]);
		} catch (Exception ex) {
			ex.printStackTrace();
			Wrapper.addChatMessage("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription() {
		return "Praise our lord and savior, OreMonger";
	}

	@Override
	public String getSyntax() {
		return "praiseore";
	}

}
