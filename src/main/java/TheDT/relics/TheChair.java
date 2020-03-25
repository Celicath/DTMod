package TheDT.relics;

import TheDT.DTModMain;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TheChair extends CustomRelic {

	public static final String RAW_ID = "TheChair";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);

	private static int prevHandSize;

	public TheChair() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.BOSS, LandingSound.HEAVY);
	}

	@Override
	public void onEquip() {
		++AbstractDungeon.player.energy.energyMaster;
	}

	@Override
	public void onUnequip() {
		--AbstractDungeon.player.energy.energyMaster;
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public void atBattleStartPreDraw() {
		prevHandSize = AbstractDungeon.player.gameHandSize;
		AbstractDungeon.player.gameHandSize = 0;
	}

	@Override
	public void atBattleStart() {
		AbstractDungeon.player.gameHandSize = prevHandSize;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new TheChair();
	}
}
