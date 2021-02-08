package durakonline.sk.durakonline;

import java.util.ArrayList;

import durakonline.sk.durakonline.other.Log;

public class Cards
{
	// тип карты
	public static final int TC_SPADE     = 1;    /// ♠
	public static final int TC_HEART     = 2;    /// ♥
	public static final int TC_CLUB      = 3;    /// ♣
	public static final int TC_DIAMOND   = 4;    /// ♦
	public static final int TC_BLANK     = 5;    /// _
	public static final int TC_SELECT    = 6;    /// _
	public static final int TC_TRANSFER  = 7;    /// _*

	/// ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠  ♠
	public static final int  SPADE_A  =  0x101;
	public static final int  SPADE_2  =  0x102;
	public static final int  SPADE_3  =  0x103;
	public static final int  SPADE_4  =  0x104;
	public static final int  SPADE_5  =  0x105;
	public static final int  SPADE_6  =  0x106;
	public static final int  SPADE_7  =  0x107;
	public static final int  SPADE_8  =  0x108;
	public static final int  SPADE_9  =  0x109;
	public static final int  SPADE_10 =  0x110;
	public static final int  SPADE_J  =  0x111;
	public static final int  SPADE_Q  =  0x112;
	public static final int  SPADE_K  =  0x113;

	/// ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥  ♥
	public static final int  HEART_A  =  0x201;
	public static final int  HEART_2  =  0x202;
	public static final int  HEART_3  =  0x203;
	public static final int  HEART_4  =  0x204;
	public static final int  HEART_5  =  0x205;
	public static final int  HEART_6  =  0x206;
	public static final int  HEART_7  =  0x207;
	public static final int  HEART_8  =  0x208;
	public static final int  HEART_9  =  0x209;
	public static final int  HEART_10 =  0x210;
	public static final int  HEART_J  =  0x211;
	public static final int  HEART_Q  =  0x212;
	public static final int  HEART_K  =  0x213;

	/// ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣  ♣
	public static final int  CLUB_A  =  0x301;
	public static final int  CLUB_2  =  0x302;
	public static final int  CLUB_3  =  0x303;
	public static final int  CLUB_4  =  0x304;
	public static final int  CLUB_5  =  0x305;
	public static final int  CLUB_6  =  0x306;
	public static final int  CLUB_7  =  0x307;
	public static final int  CLUB_8  =  0x308;
	public static final int  CLUB_9  =  0x309;
	public static final int  CLUB_10 =  0x310;
	public static final int  CLUB_J  =  0x311;
	public static final int  CLUB_Q  =  0x312;
	public static final int  CLUB_K  =  0x313;

	/// ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦  ♦
	public static final int  DIAMOND_A  =  0x401;
	public static final int  DIAMOND_2  =  0x402;
	public static final int  DIAMOND_3  =  0x403;
	public static final int  DIAMOND_4  =  0x404;
	public static final int  DIAMOND_5  =  0x405;
	public static final int  DIAMOND_6  =  0x406;
	public static final int  DIAMOND_7  =  0x407;
	public static final int  DIAMOND_8  =  0x408;
	public static final int  DIAMOND_9  =  0x409;
	public static final int  DIAMOND_10 =  0x410;
	public static final int  DIAMOND_J  =  0x411;
	public static final int  DIAMOND_Q  =  0x412;
	public static final int  DIAMOND_K  =  0x413;

	public static final int  BLANK      =  0x999;
	public static final int  TRANSFER   =  0x888;
	public static final int  SELECT_IN_BOARD = 0x000;
	public static final int  SELECT_MY_CARDS = 0x100;
	public static final int  BLANK2     =  0x99;
	public static final int  BLANK3     =  0x98;
	public static final int  BLANK4     =  0x97;
	public static final int  BLANK5     =  0x96;

	public static ArrayList<Integer> get_all_cards_from_type(int type)
	{
		ArrayList<Integer> result = new ArrayList<>();

		if( type == TC_SPADE )
		{
			result.add( SPADE_A );
			result.add( SPADE_2 );
			result.add( SPADE_3 );
			result.add( SPADE_4 );
			result.add( SPADE_5 );
			result.add( SPADE_6 );
			result.add( SPADE_7 );
			result.add( SPADE_8 );
			result.add( SPADE_9 );
			result.add( SPADE_10 );
			result.add( SPADE_J );
			result.add( SPADE_Q );
			result.add( SPADE_K );
		}
		else if( type == TC_HEART )
		{
			result.add( HEART_A );
			result.add( HEART_2 );
			result.add( HEART_3 );
			result.add( HEART_4 );
			result.add( HEART_5 );
			result.add( HEART_6 );
			result.add( HEART_7 );
			result.add( HEART_8 );
			result.add( HEART_9 );
			result.add( HEART_10 );
			result.add( HEART_J );
			result.add( HEART_Q );
			result.add( HEART_K );
		}
		else if( type == TC_CLUB )
		{
			result.add( CLUB_A );
			result.add( CLUB_2 );
			result.add( CLUB_3 );
			result.add( CLUB_4 );
			result.add( CLUB_5 );
			result.add( CLUB_6 );
			result.add( CLUB_7 );
			result.add( CLUB_8 );
			result.add( CLUB_9 );
			result.add( CLUB_10 );
			result.add( CLUB_J );
			result.add( CLUB_Q );
			result.add( CLUB_K );
		}
		else if( type == TC_DIAMOND )
		{
			result.add( DIAMOND_A );
			result.add( DIAMOND_2 );
			result.add( DIAMOND_3 );
			result.add( DIAMOND_4 );
			result.add( DIAMOND_5 );
			result.add( DIAMOND_6 );
			result.add( DIAMOND_7 );
			result.add( DIAMOND_8 );
			result.add( DIAMOND_9 );
			result.add( DIAMOND_10 );
			result.add( DIAMOND_J );
			result.add( DIAMOND_Q );
			result.add( DIAMOND_K );
		}

		return result;
	}

	public static int getIconResCard(int card_type)
	{
		switch(card_type)
		{
			case Cards.TC_SPADE:  return R.drawable.card_small_icon_spade;
			case Cards.TC_DIAMOND:  return R.drawable.card_small_icon_diamond;
			case Cards.TC_HEART:  return R.drawable.card_small_icon_heart;
			case Cards.TC_CLUB:  return R.drawable.card_small_icon_club;

			default: return 0;
		}
	}

	public static boolean testBeat(int trump_card, int beat_card, int card)
	{
		CardInfo t = Cards.getInfoCard(trump_card);
		CardInfo b = Cards.getInfoCard(beat_card);
		CardInfo c = Cards.getInfoCard(card);

		if(
				   t.type == b.type     // это козырна карта
				&& t.type == c.type     // та карта что бьют тоже козырная
				&& b.sort_num > c.sort_num
		  )
		{
			return true;
		}
		else if(
				   t.type == b.type  // это козырна карта
				&& t.type != c.type  // та карта что бьют НЕ козырная
			   )
		{
			return true;
		}
		else if(
				    t.type != b.type        // это НЕ козырна карта
				&&  b.type == c.type        // но типы карт по масти совпадают
				&& b.sort_num > c.sort_num  // и вес той что бьет больше
			)
		{
			return true;
		}

		return false;
	}

	public static CardInfo getInfoCard(int card)
	{
		switch(card)
		{
		    case Cards.SPADE_A:  return new CardInfo("A", "♠",  Cards.SPADE_A, TC_SPADE, 14);
		    case Cards.SPADE_2:  return new CardInfo("2", "♠",  Cards.SPADE_2, TC_SPADE, 2);
		    case Cards.SPADE_3:  return new CardInfo("3", "♠",  Cards.SPADE_3, TC_SPADE, 3);
		    case Cards.SPADE_4:  return new CardInfo("4", "♠",  Cards.SPADE_4, TC_SPADE, 4);
		    case Cards.SPADE_5:  return new CardInfo("5", "♠",  Cards.SPADE_5, TC_SPADE, 5);
		    case Cards.SPADE_6:  return new CardInfo("6", "♠",  Cards.SPADE_6, TC_SPADE, 6);
		    case Cards.SPADE_7:  return new CardInfo("7", "♠",  Cards.SPADE_7, TC_SPADE, 7);
		    case Cards.SPADE_8:  return new CardInfo("8", "♠",  Cards.SPADE_8, TC_SPADE, 8);
		    case Cards.SPADE_9:  return new CardInfo("9", "♠",  Cards.SPADE_9, TC_SPADE, 9);
		    case Cards.SPADE_10: return new CardInfo("10", "♠", Cards.SPADE_10, TC_SPADE, 10);
		    case Cards.SPADE_J:  return new CardInfo("J", "♠",  Cards.SPADE_J, TC_SPADE, 11);
		    case Cards.SPADE_Q:  return new CardInfo("Q", "♠",  Cards.SPADE_Q, TC_SPADE, 12);
		    case Cards.SPADE_K:  return new CardInfo("K", "♠",  Cards.SPADE_K, TC_SPADE ,13);
		    
		    case Cards.HEART_A:  return new CardInfo("A", "♥",  Cards.HEART_A, TC_HEART, 14);
		    case Cards.HEART_2:  return new CardInfo("2", "♥",  Cards.HEART_2, TC_HEART, 2);
		    case Cards.HEART_3:  return new CardInfo("3", "♥",  Cards.HEART_3, TC_HEART, 3);
		    case Cards.HEART_4:  return new CardInfo("4", "♥",  Cards.HEART_4, TC_HEART, 4);
		    case Cards.HEART_5:  return new CardInfo("5", "♥",  Cards.HEART_5, TC_HEART, 5);
		    case Cards.HEART_6:  return new CardInfo("6", "♥",  Cards.HEART_6, TC_HEART, 6);
		    case Cards.HEART_7:  return new CardInfo("7", "♥",  Cards.HEART_7, TC_HEART, 7);
		    case Cards.HEART_8:  return new CardInfo("8", "♥",  Cards.HEART_8, TC_HEART, 8);
		    case Cards.HEART_9:  return new CardInfo("9", "♥",  Cards.HEART_9, TC_HEART, 9);
		    case Cards.HEART_10: return new CardInfo("10", "♥", Cards.HEART_10, TC_HEART, 10);
		    case Cards.HEART_J:  return new CardInfo("J", "♥",  Cards.HEART_J, TC_HEART, 11);
		    case Cards.HEART_Q:  return new CardInfo("Q", "♥",  Cards.HEART_Q, TC_HEART, 12);
		    case Cards.HEART_K:  return new CardInfo("K", "♥",  Cards.HEART_K, TC_HEART, 13);
		    
		    case Cards.CLUB_A:  return new CardInfo("A", "♣",  Cards.CLUB_A, TC_CLUB, 14);
		    case Cards.CLUB_2:  return new CardInfo("2", "♣",  Cards.CLUB_2, TC_CLUB, 2);
		    case Cards.CLUB_3:  return new CardInfo("3", "♣",  Cards.CLUB_3, TC_CLUB, 3);
		    case Cards.CLUB_4:  return new CardInfo("4", "♣",  Cards.CLUB_4, TC_CLUB, 4);
		    case Cards.CLUB_5:  return new CardInfo("5", "♣",  Cards.CLUB_5, TC_CLUB, 5);
		    case Cards.CLUB_6:  return new CardInfo("6", "♣",  Cards.CLUB_6, TC_CLUB, 6);
		    case Cards.CLUB_7:  return new CardInfo("7", "♣",  Cards.CLUB_7, TC_CLUB, 7);
		    case Cards.CLUB_8:  return new CardInfo("8", "♣",  Cards.CLUB_8, TC_CLUB, 8);
		    case Cards.CLUB_9:  return new CardInfo("9", "♣",  Cards.CLUB_9, TC_CLUB, 9);
		    case Cards.CLUB_10: return new CardInfo("10", "♣", Cards.CLUB_10, TC_CLUB, 10);
		    case Cards.CLUB_J:  return new CardInfo("J", "♣",  Cards.CLUB_J, TC_CLUB, 11);
		    case Cards.CLUB_Q:  return new CardInfo("Q", "♣",  Cards.CLUB_Q, TC_CLUB, 12);
		    case Cards.CLUB_K:  return new CardInfo("K", "♣",  Cards.CLUB_K, TC_CLUB, 13);
		    
		    case Cards.DIAMOND_A:  return new CardInfo("A", "♦",  Cards.DIAMOND_A, TC_DIAMOND, 14);
		    case Cards.DIAMOND_2:  return new CardInfo("2", "♦",  Cards.DIAMOND_2, TC_DIAMOND, 2);
		    case Cards.DIAMOND_3:  return new CardInfo("3", "♦",  Cards.DIAMOND_3, TC_DIAMOND, 3);
		    case Cards.DIAMOND_4:  return new CardInfo("4", "♦",  Cards.DIAMOND_4, TC_DIAMOND, 4);
		    case Cards.DIAMOND_5:  return new CardInfo("5", "♦",  Cards.DIAMOND_5, TC_DIAMOND, 5);
		    case Cards.DIAMOND_6:  return new CardInfo("6", "♦",  Cards.DIAMOND_6, TC_DIAMOND, 6);
		    case Cards.DIAMOND_7:  return new CardInfo("7", "♦",  Cards.DIAMOND_7, TC_DIAMOND, 7);
		    case Cards.DIAMOND_8:  return new CardInfo("8", "♦",  Cards.DIAMOND_8, TC_DIAMOND, 8);
		    case Cards.DIAMOND_9:  return new CardInfo("9", "♦",  Cards.DIAMOND_9, TC_DIAMOND, 9);
		    case Cards.DIAMOND_10: return new CardInfo("10", "♦", Cards.DIAMOND_10, TC_DIAMOND, 10);
		    case Cards.DIAMOND_J:  return new CardInfo("J", "♦",  Cards.DIAMOND_J, TC_DIAMOND, 11);
		    case Cards.DIAMOND_Q:  return new CardInfo("Q", "♦",  Cards.DIAMOND_Q, TC_DIAMOND, 12);
		    case Cards.DIAMOND_K:  return new CardInfo("K", "♦",  Cards.DIAMOND_K, TC_DIAMOND, 13);

		    case Cards.BLANK:      return new CardInfo("_", "", Cards.BLANK, TC_BLANK, 0);
		    case Cards.TRANSFER:   return new CardInfo("*", "", Cards.TRANSFER, TC_TRANSFER, 0);
		    case Cards.SELECT_IN_BOARD:     return new CardInfo("_", "", Cards.SELECT_IN_BOARD, TC_SELECT, 0);
		    case Cards.SELECT_MY_CARDS:     return new CardInfo("_", "", Cards.SELECT_MY_CARDS, TC_SELECT, 0);

			default: 
					Log.i("TAG", "card not found 2");
					return null;
		}
	}
	///----------------------------------------------------------
	public static int getDrawableResId_FromOtherCard(int card)
	{
		switch(card)
		{
			case Cards.BLANK:      return R.drawable.r_card;
			case Cards.BLANK2:     return R.drawable.r_card2;
			case Cards.BLANK3:     return R.drawable.r_card3;
			case Cards.BLANK4:     return R.drawable.r_card4;
			case Cards.BLANK5:     return R.drawable.r_card5;
			case Cards.TRANSFER:   return R.drawable.transfer_card;

			default:
				Log.i("TAG", "card not found");
				return 0;
		}
	}
	///----------------------------------------------------------
	public static int getDrawableResId_FromSelectCard(int type, int card)
	{
		if( type == 1 )
		{
			switch(card)
			{
				case Cards.SELECT_IN_BOARD:   return R.drawable.select_car_din_board;
				case Cards.SELECT_MY_CARDS:   return R.drawable.select_my_card;

				default:
					Log.i("TAG", "card not found");
					return 0;
			}
		}
		else
		{
			switch(card)
			{
				case Cards.SELECT_IN_BOARD:   return R.drawable.select_car_din_board_2;
				case Cards.SELECT_MY_CARDS:   return R.drawable.select_my_card_2;

				default:
					Log.i("TAG", "card not found");
					return 0;
			}
		}
	}
	///----------------------------------------------------------
	public static int getDrawableResId_FromCard(int type_cards, int card)
	{
		if( type_cards == 1 )
		{
			switch(card)
			{
				case Cards.SPADE_A:  return R.drawable.spade_a;
				case Cards.SPADE_2:  return R.drawable.spade_2;
				case Cards.SPADE_3:  return R.drawable.spade_3;
				case Cards.SPADE_4:  return R.drawable.spade_4;
				case Cards.SPADE_5:  return R.drawable.spade_5;
				case Cards.SPADE_6:  return R.drawable.spade_6;
				case Cards.SPADE_7:  return R.drawable.spade_7;
				case Cards.SPADE_8:  return R.drawable.spade_8;
				case Cards.SPADE_9:  return R.drawable.spade_9;
				case Cards.SPADE_10: return R.drawable.spade_10;
				case Cards.SPADE_J:  return R.drawable.spade_j;
				case Cards.SPADE_Q:  return R.drawable.spade_q;
				case Cards.SPADE_K:  return R.drawable.spade_k;

				case Cards.HEART_A:  return R.drawable.heart_a;
				case Cards.HEART_2:  return R.drawable.heart_2;
				case Cards.HEART_3:  return R.drawable.heart_3;
				case Cards.HEART_4:  return R.drawable.heart_4;
				case Cards.HEART_5:  return R.drawable.heart_5;
				case Cards.HEART_6:  return R.drawable.heart_6;
				case Cards.HEART_7:  return R.drawable.heart_7;
				case Cards.HEART_8:  return R.drawable.heart_8;
				case Cards.HEART_9:  return R.drawable.heart_9;
				case Cards.HEART_10: return R.drawable.heart_10;
				case Cards.HEART_J:  return R.drawable.heart_j;
				case Cards.HEART_Q:  return R.drawable.heart_q;
				case Cards.HEART_K:  return R.drawable.heart_k;

				case Cards.CLUB_A:  return R.drawable.club_a;
				case Cards.CLUB_2:  return R.drawable.club_2;
				case Cards.CLUB_3:  return R.drawable.club_3;
				case Cards.CLUB_4:  return R.drawable.club_4;
				case Cards.CLUB_5:  return R.drawable.club_5;
				case Cards.CLUB_6:  return R.drawable.club_6;
				case Cards.CLUB_7:  return R.drawable.club_7;
				case Cards.CLUB_8:  return R.drawable.club_8;
				case Cards.CLUB_9:  return R.drawable.club_9;
				case Cards.CLUB_10: return R.drawable.club_10;
				case Cards.CLUB_J:  return R.drawable.club_j;
				case Cards.CLUB_Q:  return R.drawable.club_q;
				case Cards.CLUB_K:  return R.drawable.club_k;

				case Cards.DIAMOND_A:  return R.drawable.diamond_a;
				case Cards.DIAMOND_2:  return R.drawable.diamond_2;
				case Cards.DIAMOND_3:  return R.drawable.diamond_3;
				case Cards.DIAMOND_4:  return R.drawable.diamond_4;
				case Cards.DIAMOND_5:  return R.drawable.diamond_5;
				case Cards.DIAMOND_6:  return R.drawable.diamond_6;
				case Cards.DIAMOND_7:  return R.drawable.diamond_7;
				case Cards.DIAMOND_8:  return R.drawable.diamond_8;
				case Cards.DIAMOND_9:  return R.drawable.diamond_9;
				case Cards.DIAMOND_10: return R.drawable.diamond_10;
				case Cards.DIAMOND_J:  return R.drawable.diamond_j;
				case Cards.DIAMOND_Q:  return R.drawable.diamond_q;
				case Cards.DIAMOND_K:  return R.drawable.diamond_k;

				default:
					Log.i("TAG", "card not found");
					return 0;
			}
		}
		else if( type_cards == 2 )
		{
			switch(card)
			{
				case Cards.SPADE_A:  return R.drawable.spade_a_2;
				case Cards.SPADE_2:  return R.drawable.spade_2_2;
				case Cards.SPADE_3:  return R.drawable.spade_3_2;
				case Cards.SPADE_4:  return R.drawable.spade_4_2;
				case Cards.SPADE_5:  return R.drawable.spade_5_2;
				case Cards.SPADE_6:  return R.drawable.spade_6_2;
				case Cards.SPADE_7:  return R.drawable.spade_7_2;
				case Cards.SPADE_8:  return R.drawable.spade_8_2;
				case Cards.SPADE_9:  return R.drawable.spade_9_2;
				case Cards.SPADE_10: return R.drawable.spade_10_2;
				case Cards.SPADE_J:  return R.drawable.spade_j_2;
				case Cards.SPADE_Q:  return R.drawable.spade_q_2;
				case Cards.SPADE_K:  return R.drawable.spade_k_2;

				case Cards.HEART_A:  return R.drawable.heart_a_2;
				case Cards.HEART_2:  return R.drawable.heart_2_2;
				case Cards.HEART_3:  return R.drawable.heart_3_2;
				case Cards.HEART_4:  return R.drawable.heart_4_2;
				case Cards.HEART_5:  return R.drawable.heart_5_2;
				case Cards.HEART_6:  return R.drawable.heart_6_2;
				case Cards.HEART_7:  return R.drawable.heart_7_2;
				case Cards.HEART_8:  return R.drawable.heart_8_2;
				case Cards.HEART_9:  return R.drawable.heart_9_2;
				case Cards.HEART_10: return R.drawable.heart_10_2;
				case Cards.HEART_J:  return R.drawable.heart_j_2;
				case Cards.HEART_Q:  return R.drawable.heart_q_2;
				case Cards.HEART_K:  return R.drawable.heart_k_2;

				case Cards.CLUB_A:  return R.drawable.club_a_2;
				case Cards.CLUB_2:  return R.drawable.club_2_2;
				case Cards.CLUB_3:  return R.drawable.club_3_2;
				case Cards.CLUB_4:  return R.drawable.club_4_2;
				case Cards.CLUB_5:  return R.drawable.club_5_2;
				case Cards.CLUB_6:  return R.drawable.club_6_2;
				case Cards.CLUB_7:  return R.drawable.club_7_2;
				case Cards.CLUB_8:  return R.drawable.club_8_2;
				case Cards.CLUB_9:  return R.drawable.club_9_2;
				case Cards.CLUB_10: return R.drawable.club_10_2;
				case Cards.CLUB_J:  return R.drawable.club_j_2;
				case Cards.CLUB_Q:  return R.drawable.club_q_2;
				case Cards.CLUB_K:  return R.drawable.club_k_2;

				case Cards.DIAMOND_A:  return R.drawable.diamond_a_2;
				case Cards.DIAMOND_2:  return R.drawable.diamond_2_2;
				case Cards.DIAMOND_3:  return R.drawable.diamond_3_2;
				case Cards.DIAMOND_4:  return R.drawable.diamond_4_2;
				case Cards.DIAMOND_5:  return R.drawable.diamond_5_2;
				case Cards.DIAMOND_6:  return R.drawable.diamond_6_2;
				case Cards.DIAMOND_7:  return R.drawable.diamond_7_2;
				case Cards.DIAMOND_8:  return R.drawable.diamond_8_2;
				case Cards.DIAMOND_9:  return R.drawable.diamond_9_2;
				case Cards.DIAMOND_10: return R.drawable.diamond_10_2;
				case Cards.DIAMOND_J:  return R.drawable.diamond_j_2;
				case Cards.DIAMOND_Q:  return R.drawable.diamond_q_2;
				case Cards.DIAMOND_K:  return R.drawable.diamond_k_2;

				default:
					Log.i("TAG", "card not found");
					return 0;
			}
		}
		else
		{
			switch(card)
			{
				case Cards.SPADE_A:  return R.drawable.spade_a_3;
				case Cards.SPADE_2:  return R.drawable.spade_2_3;
				case Cards.SPADE_3:  return R.drawable.spade_3_3;
				case Cards.SPADE_4:  return R.drawable.spade_4_3;
				case Cards.SPADE_5:  return R.drawable.spade_5_3;
				case Cards.SPADE_6:  return R.drawable.spade_6_3;
				case Cards.SPADE_7:  return R.drawable.spade_7_3;
				case Cards.SPADE_8:  return R.drawable.spade_8_3;
				case Cards.SPADE_9:  return R.drawable.spade_9_3;
				case Cards.SPADE_10: return R.drawable.spade_10_3;
				case Cards.SPADE_J:  return R.drawable.spade_j_3;
				case Cards.SPADE_Q:  return R.drawable.spade_q_3;
				case Cards.SPADE_K:  return R.drawable.spade_k_3;

				case Cards.HEART_A:  return R.drawable.heart_a_3;
				case Cards.HEART_2:  return R.drawable.heart_2_3;
				case Cards.HEART_3:  return R.drawable.heart_3_3;
				case Cards.HEART_4:  return R.drawable.heart_4_3;
				case Cards.HEART_5:  return R.drawable.heart_5_3;
				case Cards.HEART_6:  return R.drawable.heart_6_3;
				case Cards.HEART_7:  return R.drawable.heart_7_3;
				case Cards.HEART_8:  return R.drawable.heart_8_3;
				case Cards.HEART_9:  return R.drawable.heart_9_3;
				case Cards.HEART_10: return R.drawable.heart_10_3;
				case Cards.HEART_J:  return R.drawable.heart_j_3;
				case Cards.HEART_Q:  return R.drawable.heart_q_3;
				case Cards.HEART_K:  return R.drawable.heart_k_3;

				case Cards.CLUB_A:  return R.drawable.club_a_3;
				case Cards.CLUB_2:  return R.drawable.club_2_3;
				case Cards.CLUB_3:  return R.drawable.club_3_3;
				case Cards.CLUB_4:  return R.drawable.club_4_3;
				case Cards.CLUB_5:  return R.drawable.club_5_3;
				case Cards.CLUB_6:  return R.drawable.club_6_3;
				case Cards.CLUB_7:  return R.drawable.club_7_3;
				case Cards.CLUB_8:  return R.drawable.club_8_3;
				case Cards.CLUB_9:  return R.drawable.club_9_3;
				case Cards.CLUB_10: return R.drawable.club_10_3;
				case Cards.CLUB_J:  return R.drawable.club_j_3;
				case Cards.CLUB_Q:  return R.drawable.club_q_3;
				case Cards.CLUB_K:  return R.drawable.club_k_3;

				case Cards.DIAMOND_A:  return R.drawable.diamond_a_3;
				case Cards.DIAMOND_2:  return R.drawable.diamond_2_3;
				case Cards.DIAMOND_3:  return R.drawable.diamond_3_3;
				case Cards.DIAMOND_4:  return R.drawable.diamond_4_3;
				case Cards.DIAMOND_5:  return R.drawable.diamond_5_3;
				case Cards.DIAMOND_6:  return R.drawable.diamond_6_3;
				case Cards.DIAMOND_7:  return R.drawable.diamond_7_3;
				case Cards.DIAMOND_8:  return R.drawable.diamond_8_3;
				case Cards.DIAMOND_9:  return R.drawable.diamond_9_3;
				case Cards.DIAMOND_10: return R.drawable.diamond_10_3;
				case Cards.DIAMOND_J:  return R.drawable.diamond_j_3;
				case Cards.DIAMOND_Q:  return R.drawable.diamond_q_3;
				case Cards.DIAMOND_K:  return R.drawable.diamond_k_3;

				default:
					Log.i("TAG", "card not found");
					return 0;
			}
		}
	}
}
