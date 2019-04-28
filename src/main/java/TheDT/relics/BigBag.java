package TheDT.relics;

import TheDT.DTMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BigBag extends CustomRelic {

	private static final String RAW_ID = "BigBag";
	public static final String ID = DTMod.makeID(RAW_ID);
	public static final String IMG = DTMod.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTMod.GetRelicOutlinePath(RAW_ID);
	private static final int PER = 7;

	public BigBag() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.RARE, LandingSound.HEAVY);
	}


	public void atPreBattle() {
		if (AbstractDungeon.player.masterDeck.size() >= PER)
			this.flash();
		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.masterDeck.size() / PER));
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + PER + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new BigBag();
	}
}
