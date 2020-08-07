package TheDT.relics;

import TheDT.DTModMain;
import TheDT.cards.HardSkin;
import TheDT.characters.DragonTamer;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AncientBone extends CustomRelic {

	public static final String RAW_ID = "AncientBone";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);

	public AncientBone() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.UNCOMMON, LandingSound.HEAVY);
	}

	@Override
	public void atBattleStartPreDraw() {
		AbstractPlayer p = AbstractDungeon.player instanceof DragonTamer ? ((DragonTamer) AbstractDungeon.player).dragon : AbstractDungeon.player;

		AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new HardSkin(), false));
		AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(p, this));
		grayscale = true;
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new AncientBone();
	}
}
