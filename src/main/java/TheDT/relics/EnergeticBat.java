package TheDT.relics;

import TheDT.DTMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EnergeticBat extends CustomRelic {

	private static final String RAW_ID = "EnergeticBat";
	public static final String ID = DTMod.makeID(RAW_ID);
	public static final String IMG = DTMod.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTMod.GetRelicOutlinePath(RAW_ID);

	public EnergeticBat() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.RARE, LandingSound.HEAVY);
	}

	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card.hasTag(AbstractCard.CardTags.STRIKE)) {
			this.flash();
			AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new EnergeticBat();
	}
}
