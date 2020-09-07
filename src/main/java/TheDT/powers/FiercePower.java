package TheDT.powers;

import TheDT.DTModMain;
import TheDT.Interfaces.CreateBurnPower;
import TheDT.Interfaces.RecoloredPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;

public class FiercePower extends AbstractPower implements CreateBurnPower, RecoloredPower {
	public static final String RAW_ID = "FiercePower";
	public static final String POWER_ID = DTModMain.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private boolean isPlayerTurn = true;

	public FiercePower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.loadRegion("deva");
	}

	@Override
	public Color getIconColor() {
		return new Color(1.0f, 0.9f, 0.35f, 1.0f);
	}

	@Override
	public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
		super.renderIcons(sb, x, y, new Color(1.0f, 0.9f, 0.35f, c.a));
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
		} else {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
		}
	}

	@Override
	public void atStartOfTurn() {
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				isPlayerTurn = true;
				isDone = true;
			}
		});
	}

	@Override
	public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
		isPlayerTurn = false;
	}

	@Override
	public void onBurnCreated() {
		flash();
		if (isPlayerTurn) {
			addToBot(new GainEnergyAction(amount));
		} else {
			addToTop(new ApplyPowerAction(owner, owner, new EnergizedPower(owner, amount), amount));
		}
	}
}
