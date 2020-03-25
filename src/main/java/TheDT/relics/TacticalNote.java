package TheDT.relics;

import TheDT.DTModMain;
import TheDT.cards.*;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TacticalNote extends CustomRelic implements CustomSavable<Void> {

	public static final String RAW_ID = "TacticalNote";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);

	public TacticalNote() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.UNCOMMON, LandingSound.FLAT);
		tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[2]));
	}

	@Override
	public void onEquip() {
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c instanceof SwitchingTactic) {
				c.modifyCostForCombat(-9);
			}
		}
	}

	@Override
	public void onUnequip() {
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c instanceof SwitchingTactic) {
				c.cost = c.costForTurn = SwitchingTactic.COST;
			}
		}
	}

	@Override
	public Void onSave() {
		return null;
	}

	@Override
	public void onLoad(Void v) {
		if (AbstractDungeon.player != null) {
			for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
				if (c instanceof SwitchingTactic) {
					c.modifyCostForCombat(-9);
				}
			}
		}
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card instanceof SwitchingTactic || card instanceof RunningTactic || card instanceof TacticRecycle || card instanceof BizarreTactic || card instanceof OpeningTactic) {
			AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new TacticalNote();
	}
}
