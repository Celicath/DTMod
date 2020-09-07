package TheDT.powers;

import TheDT.DTModMain;
import TheDT.Interfaces.RecoloredPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AggroFixPower extends AbstractPower implements RecoloredPower {
	public static final String RAW_ID = "AggroFixPower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public AggroFixPower(AbstractCreature owner) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.loadRegion("no_stance");
	}

	@Override
	public Color getIconColor() {
		return new Color(0.4f, 1.0f, 0.5f, 1.0f);
	}

	@Override
	public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
		super.renderIcons(sb, x, y, new Color(0.4f, 1.0f, 0.5f, c.a));
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

	@Override
	public void atStartOfTurn() {
		addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
	}
}
