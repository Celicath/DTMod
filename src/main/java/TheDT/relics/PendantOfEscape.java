package TheDT.relics;

import TheDT.DTMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class PendantOfEscape extends CustomRelic implements ClickableRelic {

	private static final String RAW_ID = "PendantOfEscape";
	public static final String ID = DTMod.makeID(RAW_ID);
	public static final String IMG = DTMod.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTMod.GetRelicOutlinePath(RAW_ID);
	public static boolean disabled = false;
	public boolean alreadyUsed;

	public PendantOfEscape() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.SHOP, LandingSound.MAGICAL);
	}

	@Override
	public void onRightClick() {
		if (!isObtained || alreadyUsed) {
			return;
		}

		if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
				AbstractDungeon.getCurrRoom().monsters != null && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() &&
				!AbstractDungeon.actionManager.turnHasEnded && !disabled
		) {
			alreadyUsed = true;
			flash();
			stopPulse();
			AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, 5));
			AbstractDungeon.actionManager.addToTop(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.hand.size(), false));
		}
	}

	@Override
	public void atPreBattle() {
		alreadyUsed = false;
		beginLongPulse();
	}

	@Override
	public void onVictory() {
		stopPulse();
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public void atTurnStartPostDraw() {
		disabled = false;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new PendantOfEscape();
	}
}
