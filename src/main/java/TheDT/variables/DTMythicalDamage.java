package TheDT.variables;

import TheDT.cards.AbstractDTCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class DTMythicalDamage extends DynamicVariable {
	@Override
	public String key() {
		if (Settings.language == Settings.GameLanguage.ZHS) {
			// Hopefully this hack will be gone with the base game update
			return "T:TH";
		}
		return "DT:THD";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).isDTMythicalDamageModified;
		} else {
			return false;
		}
	}

	@Override
	public int value(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).dtMythicalDamage;
		} else {
			return 0;
		}
	}

	@Override
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).dtBaseMythicalDamage;
		} else {
			return 0;
		}
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).upgradedDTMythicalDamage;
		} else {
			return false;
		}
	}
}
