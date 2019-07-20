package TheDT.variables;

import TheDT.cards.AbstractDTCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class DTDragonDamage extends DynamicVariable {
	@Override
	public String key() {
		if (Settings.language == Settings.GameLanguage.ZHS) {
			// Hopefully this hack will be gone with the base game update
			return "T:THE";
		}
		return "DT:THD";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).isDTDragonDamageModified;
		} else {
			return false;
		}
	}

	@Override
	public int value(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).dtDragonDamage;
		} else {
			return 0;
		}
	}

	@Override
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).dtBaseDragonDamage;
		} else {
			return 0;
		}
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractDTCard) {
			return ((AbstractDTCard) card).upgradedDTDragonDamage;
		} else {
			return false;
		}
	}
}
