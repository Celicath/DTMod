package TheDT.relics;

import TheDT.DTModMain;
import TheDT.cards.TargetDefense;
import TheDT.utils.RelicHelper;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BasicTextbook extends CustomRelic {

	public static final String RAW_ID = "BasicTextbook";
	public static final String ID = DTModMain.makeID(RAW_ID);
	public static final String IMG = DTModMain.GetRelicPath(RAW_ID);
	public static final String OUTLINE = DTModMain.GetRelicOutlinePath(RAW_ID);

	public static final int BONUS = 2;

	public BasicTextbook() {
		super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.COMMON, LandingSound.FLAT);
		RelicHelper.removeStrikeTip(this);
	}

	@Override
	public float atDamageModify(float damage, AbstractCard c) {
		if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
			damage += BONUS;
		}
		return damage;
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + BONUS + DESCRIPTIONS[1];
	}

	@Override
	public boolean canSpawn() {
		if (AbstractDungeon.player == null) {
			return true;
		}
		int count = 0;
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c instanceof TargetDefense) {
				count++;
			}
		}
		return count >= 2;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new BasicTextbook();
	}
}
