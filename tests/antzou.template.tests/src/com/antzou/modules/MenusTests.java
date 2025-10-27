package com.antzou.modules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class MenusTests {

	private static SWTBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		// don't use SWTWorkbenchBot here which relies on Platform 3.x
		bot = new SWTBot();
	}

	@Test
	public void testInputInformationAndSave() {
		// 等待应用加载完成
		bot.sleep(2000);
		
		// 1. 点击导航树中的"信息录入"
		SWTBotTree navTree = bot.tree();
		SWTBotTreeItem inputItem = navTree.getTreeItem("信息录入");
		inputItem.doubleClick();
		
		// 等待部件加载
		bot.sleep(1000);
		
		// 2. 输入姓名
		SWTBotText nameText = bot.text(0); // 第一个文本框对应姓名
		nameText.setText("张三");
		
		// 3. 输入年龄
		SWTBotText ageText = bot.text(1); // 第二个文本框对应年龄
		ageText.setText("25");
		
		// 4. 输入电话
		SWTBotText phoneText = bot.text(2); // 第三个文本框对应电话
		phoneText.setText("13800138000");
		
		// 5. 点击保存按钮
		SWTBotButton saveButton = bot.button("保存");
		saveButton.click();
		
		// 等待保存操作完成
		bot.sleep(2000);
		
		// 验证：这里可以添加一些验证逻辑，比如检查控制台输出或者界面状态
		System.out.println("信息录入测试完成");
	}

	// 可选：添加一个清理方法，在测试后重置状态
	@Test
	public void testInputWithDifferentData() {
		// 等待应用加载完成
		bot.sleep(2000);
		
		// 点击信息录入
		SWTBotTree navTree = bot.tree();
		SWTBotTreeItem inputItem = navTree.getTreeItem("信息录入");
		inputItem.doubleClick();
		
		bot.sleep(1000);
		
		// 输入不同的测试数据
		bot.text(0).setText("李四");
		bot.text(1).setText("30");
		bot.text(2).setText("13900139000");
		
		// 点击保存
		bot.button("保存").click();
		
		bot.sleep(1000);
		
		System.out.println("第二种数据输入测试完成");
	}
}