package TheDT.relics;

import TheDT.DTModMain;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.BetterDiscardPileToHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class SolidMemories extends CustomRelic {

	public static final String RAW_ID = "SolidMemories";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);
	public static final int TURN = 2;

	public SolidMemories() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.RARE, LandingSound.CLINK);
	}

	@Override
	public void atBattleStart() {
		this.counter = 0;
	}

	@Override
	public void atTurnStart() {
		++this.counter;
		if (this.counter == TURN) {
			flash();
			addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			addToBot(new BetterDiscardPileToHandAction(1, 0));
			stopPulse();
			grayscale = true;
		}
	}

	@Override
	public void justEnteredRoom(AbstractRoom room) {
		grayscale = false;
	}

	@Override
	public void onVictory() {
		this.counter = -1;
		this.stopPulse();
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + TURN + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new SolidMemories();
	}
}
