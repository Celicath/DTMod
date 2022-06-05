package TheDT.relics;

import TheDT.DTModMain;
import TheDT.cards.*;
import TheDT.patches.CustomTags;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TacticalNote extends CustomRelic {

	public static final String RAW_ID = "TacticalNote";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);

	public static String[] tacticCardsId = new String[]{
		DTModMain.makeID(SwitchingTactic.RAW_ID),
		DTModMain.makeID(BizarreTactic.RAW_ID),
		DTModMain.makeID(RunningTactic.RAW_ID),
		DTModMain.makeID(EchoTactic.RAW_ID),
		DTModMain.makeID(OpeningTactic.RAW_ID)
	};

	public TacticalNote() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.UNCOMMON, LandingSound.FLAT);
		String detail = DESCRIPTIONS[2];
		for (int i = 3; i < DESCRIPTIONS.length; i++) {
			detail += " NL " + DESCRIPTIONS[i];
		}
		tips.add(new PowerTip(DESCRIPTIONS[1], detail));
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card.hasTag(CustomTags.DT_TACTIC)) {
			flash();
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
