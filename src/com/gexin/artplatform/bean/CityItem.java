package com.gexin.artplatform.bean;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;

import com.gexin.artplatform.utils.PinYin;

@SuppressLint("DefaultLocale")
public class CityItem implements Comparable<CityItem>{

	private static String[] citys = { "�Ϸ�", "����", "����", "����", "����", "����", "����",
			"��ɽ", "�ߺ�", "ͭ��", "����", "��ɽ", "����", "����", "����", "����", " ����", "��ƽ",
			"����", "����", "Ȫ��", "����", "����", "����", "����", "������", "���", "����", "��ˮ",
			"��Ȫ", "��Ҵ", "����", "����", "ƽ��", "����", "¤��", "����", "����", "����", "��Զ",
			"�ع�", "��Դ", "÷��", "����", "��ͷ", "����", "��β", "����", "��ݸ", "�麣", "��ɽ",
			"����", "��ɽ", "����", "�Ƹ�", "����", "ï��", "տ��", "����", "����ˮ", "����", "��˳",
			"�Ͻ�", "ͭ��", "ʯ��ׯ", "����", "��ɽ", "����", "�ػʵ�", "��̨", "�żҿ�", "�е�",
			"����", "�ȷ�", "��ˮ", "������", "�������", "�ں�", "����", "����", "�׸�", "��ľ˹",
			"˫Ѽɽ", "��̨��", "����", "ĵ����", "�绯", "֣��", "����", "����", "ƽ��ɽ", "����",
			"�ױ�", "����", "����", "���", "���", "���", "����Ͽ", "����", "����", "�ܿ�", "פ���",
			"����", "�人", "ʮ��", "����", "����", "Т��", "�Ƹ�", "����", "��ʯ", "����", "����",
			"�˲�", "����", "��ɳ", "����", "�żҽ�", "����", "����", "����", "����", "��̶", "����",
			"����", "����", "����", "¦��", "����", "����", "�׳�", "��ԭ", "��ƽ", "��Դ", "ͨ��",
			"��ɽ", "�ϲ�", "�Ž�", "������", "ӥ̶", "����", "Ƽ��", "����", "����", "����", "�˴�",
			"���� ", "�Ͼ�", "����", "���Ƹ�", "��Ǩ", "����", "�γ�", "����", "̩��", "��ͨ", "��",
			"����", "����", "����", "����", "����", "����", "����", "����", "��˳", "��Ϫ", "����",
			"��ɽ", "����", "Ӫ��", "�̽�", "����", "��«��", "����", "�ൺ", "�ĳ�", "����", "��Ӫ",
			"�Ͳ�", "Ϋ��", "��̨", "����", "����", "����", "��ׯ", "����", "̩��", "����", "����",
			"����", "����", "�Ӱ�", "ͭ��", "μ��", "����", "����", "����", "����", "����", "����",
			"̫ԭ", "��ͬ", "˷��", "��Ȫ", "����", "����", "����", "����", "����", "�ٷ�", "�˳�",
			"�ɶ�", "��Ԫ", "����", "����", "�ϳ�", "�㰲", "����", "�ڽ�", "��ɽ", "�Թ�", "����",
			"�˱�", "��֦��", "����", "����", "����", "üɽ", "�Ű�", "����", "����", "��Ϫ", "����",
			"��ͨ", "�ն�", "�ٲ�", "��ɽ", "����", "����", "����", "����", "��ɽ", "����", "����",
			"��", "̨��", "����", "��ˮ", "����", "����", "����", "����", "��ɳ", "����", "����",
			"����", "����", "����", "���", "����", "����", "����", "���Ǹ�", "����", "��ɫ", "�ӳ�",
			"����", "����", "���ͺ���", "��ͷ", "�ں�", "���", "���ױ���", "ͨ��", "�����첼", "������˹",
			"�����׶�", "����", "ʯ��ɽ", "����", "����", "��ԭ", "����", "�տ���", "����", "��֥",
			"��³ľ��", "��������", "��³��", "���", "���T " };

	private String name;
	private String fullPinyin;
	private String simplePinyin;
	private String sortLetter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		try {
			this.fullPinyin = PinYin.getPinYin(name);
			this.simplePinyin = PinYin.getSimplePinYin(name);
			this.sortLetter = fullPinyin.substring(0, 1).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFullPinyin() {
		return fullPinyin;
	}

	public String getSimplePinyin() {
		return simplePinyin;
	}

	public String getSortLetter() {
		return sortLetter;
	}

	public static List<CityItem> getCityList() {
		List<CityItem> list = new ArrayList<CityItem>();
		for (String str : citys) {
			CityItem item = new CityItem();
			item.setName(str);
			list.add(item);
		}
		return list;
	}

	@Override
	public String toString() {
		return "CityItem [name=" + name + ", fullPinyin=" + fullPinyin
				+ ", simplePinyin=" + simplePinyin + ", sortLetter="
				+ sortLetter + "]";
	}

	@Override
	public int compareTo(CityItem arg0) {
		
		return this.fullPinyin.compareTo(arg0.fullPinyin);
	}

}
