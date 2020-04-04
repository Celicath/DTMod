package TheDT.relics;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class SwitchButton extends CustomRelic implements ClickableRelic {

	private static final String RAW_ID = "SwitchButton";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);
	public static boolean disabled = false;
	public boolean alreadyUsed;

	public SwitchButton() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.SHOP, LandingSound.MAGICAL);
	}

	@Override
	public void onRightClick() {
		if (!isObtained || alreadyUsed || !(AbstractDungeon.player instanceof DragonTamer)) {
			return;
		}

		if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
				AbstractDungeon.getCurrRoom().monsters != null && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() &&
				!AbstractDungeon.actionManager.turnHasEnded && !disabled
		) {
			Dragon d = DragonTamer.getLivingDragon();
			if (d != null) {
				alreadyUsed = true;
				flash();
				stopPulse();
				grayscale = true;

				DragonTamer dtp = (DragonTamer) AbstractDungeon.player;
				if (dtp.aggro == 0) {
					dtp.setFront(dtp.front == d ? dtp : d);
				} else {
					dtp.setAggro(-dtp.aggro);
				}
			}
		}
	}

	@Override
	public void justEnteredRoom(AbstractRoom room) {
		grayscale = false;
	}

	@Override
	public void atPreBattle() {
		alreadyUsed = false;
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
		return new SwitchButton();
	}
}
