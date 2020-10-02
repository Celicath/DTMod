package TheDT.screens;

import TheDT.DTModMain;
import basemod.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;

import java.util.Arrays;

public class DTModPanel extends ModPanel {
	String[] configTexts;
	ModLabeledToggleButton[] enemyTargetButton = new ModLabeledToggleButton[4];
	Hitbox[] hbs = new Hitbox[4];
	int hovering = 3;
	float time = 0.0f;

	public void initializeUIElements() {
		if (configTexts == null) {
			configTexts = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("Config")).TEXT;
		}

		ModLabel l = new ModLabel(configTexts[0], 400.0f, 700.0f, this, (label) -> {
		});
		addUIElement(l);

		for (int i = 0; i < 4; i++) {
			int finalI = i;
			enemyTargetButton[i] = new ModLabeledToggleButton(configTexts[i + 1],
					400.0f, 580.0f - 60.0f * i, Settings.CREAM_COLOR, FontHelper.charDescFont,
					DTModMain.enemyTargetDisplayConfig[i], this, (label) -> {
			}, (button) -> {
				DTModMain.enemyTargetDisplayConfig[finalI] = button.enabled;
				DTModMain.saveConfig();
			});
			hbs[i] = (Hitbox) ReflectionHacks.getPrivate(enemyTargetButton[i].toggle, ModToggleButton.class, "hb");

			addUIElement(enemyTargetButton[i]);
		}

		ModLabeledButton allButton = new ModLabeledButton(configTexts[5],
				750.0f, 670.0f, Settings.CREAM_COLOR.cpy(), Color.YELLOW.cpy(), FontHelper.buttonLabelFont,
				this, (button) -> {
			Arrays.fill(DTModMain.enemyTargetDisplayConfig, true);
			for (int i = 0; i < 4; i++) {
				enemyTargetButton[i].toggle.enabled = true;
			}
			DTModMain.saveConfig();
		});

		float w = (float) ReflectionHacks.getPrivate(allButton, ModLabeledButton.class, "w");
		ModLabeledButton noneButton = new ModLabeledButton(configTexts[6],
				800.0f + w / Settings.scale, 670.0f, Settings.CREAM_COLOR.cpy(), Color.YELLOW.cpy(), FontHelper.buttonLabelFont,
				this, (button) -> {
			Arrays.fill(DTModMain.enemyTargetDisplayConfig, false);
			for (int i = 0; i < 4; i++) {
				enemyTargetButton[i].toggle.enabled = false;
			}
			DTModMain.saveConfig();
		});
		addUIElement(allButton);
		addUIElement(noneButton);
	}

	@Override
	public void update() {
		super.update();

		time += Gdx.graphics.getDeltaTime();
		for (int i = 0; i < 4; i++) {
			if (hbs[i].hovered && hovering != i) {
				hovering = i;
				time = 0.0f;
				break;
			}
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);


	}
}
