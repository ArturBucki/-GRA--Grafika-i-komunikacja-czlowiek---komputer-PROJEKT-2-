import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener									// Rozszerza klasie GamePanel o JPanel implementuje ActionListener
{
	private static final long serialVersionUID = 1L;					
	static final int SCREEN_WIDTH = 600;														// Szerokosc okna
	static final int SCREEN_HEIGHT = 600;														// Wysokosc okna
	static final int UNIT_SIZE = 10;															// Wielkosc jednostki, kwadratu
	static final int GAME_UNITS = (SCREEN_WIDTH / UNIT_SIZE) * (SCREEN_HEIGHT / UNIT_SIZE);		// Podzielenie ekarnu na kwadraty
	static int DELAY = 35; 					// 	Opoznienie 35ms
	final int x[] = new int[GAME_UNITS];  	//	Tabllica wspolrzednych x
	final int y[] = new int[GAME_UNITS];  	//	Tablica wspolrzednych y
	int bodyParts = 6; 						// 	Cialo snake'a
	int applesEaten; 						// 	Ilosc zjedzonych jablek
	int appleX;								// 	Wspolrzedne jablka x
	int appleY; 							//	Wspolrzedne jablka y
	int superApplex;						//	Wspolrzedne jablka super x
	int superAppley;						//	Wspolrzedne jablka super y
	char direction = 'R'; 					//	Okreslenie poruszania sie snake'a R = Right
	boolean running = false; 				//	Czy gra sie toczy
	static boolean gameOn = false; 			// 	Czy gra jest wlaczona
	boolean text = true;					//	Czy text jest wlaczony
	Timer timer;							//	Timer okresla predkosc gry
	Random random; 							//	Randomowa wartosc
	JButton easy, medium, hard;				//	Klawisze pozwalajace na wybranie poziomu trudnosci
	
	
	GamePanel()								
	{
		random = new Random();													//	Przpisanie zmiennej random wartosci losowej
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));  	//	Ustawienie okna
		this.setBackground(Color.black); 										// 	Kolor tla czarny
		this.setFocusable(true); 												//	Mozliwosc klikniecia w okno
		this.addKeyListener(new MyKeyAdapter()); 								// 	Dodanie klawiszy do sterowania
		
		easy = new JButton("Easy");												//	Klawisze, poziomy trudnosci
		medium = new JButton("Medium");
		hard = new JButton("Hard");
		this.add(easy);
		this.add(medium);
		this.add(hard);
		
		
																				//	 	Poszczegolne poziomy trudnosci i ich ustawienia
		easy.addActionListener(new ActionListener()								// easy
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{
				DELAY = 50;
				text = false;
				running = true;
				startGame();
				easy.hide();
				medium.hide();
				hard.hide();
			}
		
		});
		
		medium.addActionListener(new ActionListener()							// medium
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{
				DELAY = 35;
				text = false;
				running = true;
				startGame();
				easy.hide();
				medium.hide();
				hard.hide();
			}
		});
		
		hard.addActionListener(new ActionListener()								// hard
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{
				DELAY = 20;
				text = false;
				running = true;
				startGame();
				easy.hide();
				medium.hide();
				hard.hide();
			}
		});
	}
	
	public void startGame()						//	Metoda: Rozpoczecie gry
	{
		newApple();								//	Stworzenie jablka
		newSuperApple();						// 	Stworzenie jablka super
		running = true;							//	Informacja: Czy gra sie toczy ?
		timer = new Timer(DELAY, this);			//	Dodanie timera
		timer.start();							// 	Start gry	
	}
	
	public void pause()							//	Metoda: pause
	{
		GamePanel.gameOn = true;				
		timer.stop();							// 	Wylaczenie timera, powoduje zastopowanie gierki
	}
	
	public void resume()						// 	Metoda: Resume
	{
		GamePanel.gameOn = false;				
		timer.start();							// 	Wlaczenie timera, powoduje start gry
	}
	
	@Override
	public void paintComponent(Graphics g)		
	{
		super.paintComponent(g);				// Wywolanie konstruktora klasy rozszerzajacej
		draw(g);								// Rysowanie 
	}
		
	public void draw(Graphics g)  				// Metoda rysujaca
	{
		if(running)
		{
			/*
			for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++)
			{
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);	// Rysuje linie pomocnicze
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);  // Rysuje linie pomocnicze
			}
			*/
			
			g.setColor(Color.red);											//	Ustawienie koloru jabkla
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); 		
			
			g.setColor(Color.blue);											//	Ustawienie koloru jablka super
			g.fillOval(superApplex, superAppley, UNIT_SIZE, UNIT_SIZE); 
		
			
			
			for(int i = 0; i < bodyParts; i++)						// Ustawienie koloru ciala weza
			{
				if(i == 0)
				{
					g.setColor(Color.green); 
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			
				else
				{
					g.setColor(Color.green.darker().darker());
				//	g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE); 
				}
			}
			
			g.setColor(Color.red);									// NAPIS SCORE
			g.setFont(new Font("Bebas", Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); 
			
		
			g.setColor(Color.WHITE);								// NAPIS PAUSE
			g.setFont(new Font("Bebas", Font.PLAIN, 20));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("Press p to Pause", (1050 - metrics2.stringWidth("Press p to Pause")) / 2, g.getFont().getSize()); 
		}
		
		else if(!text)				// Napis koncowy jesli przegramy
		{
			gameOver(g);
		}
		
		else if(!running)			// Napis poczatkowy do wybrania gry
		{
			g.setColor(Color.red);	
			g.setFont(new Font("Bebas", Font.BOLD, 75));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Choose", (SCREEN_WIDTH - metrics.stringWidth("Choose")) / 2, SCREEN_HEIGHT / 2); 
		}
	}
	
	public void newApple()			// Tworzymy nowe jablko
	{
		appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;	
		appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;	
	}
	
	public void newSuperApple()		// Tworzymy nowe jablko super
	{
		superApplex = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;	
		superAppley = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;	
	}
	
	public void move() 				// Poruszanie sie weza
	{
		for(int i = bodyParts; i > 0; i--)		// Cialo weza podaza za glowa
		{
			x[i] = x[i - 1]; 
			y[i] = y[i - 1]; 
		}
		
		switch(direction)	
		{
			case 'U': y[0] = y[0] - UNIT_SIZE; break;		// U = UP
			case 'D': y[0] = y[0] + UNIT_SIZE; break;		// D = DOWN
			case 'L': x[0] = x[0] - UNIT_SIZE; break;		// L = LEFT
			case 'R': x[0] = x[0] + UNIT_SIZE; break;		// R = RIGHT
		}
	}
	
	public void checkApple()  								// Sprawdza czy jablko zostalo zjedzone przez weza
	{
		if((x[0] == appleX) && (y[0] == appleY))			
		{
			bodyParts++;									// Dodaje partie weza
			applesEaten++;									// Dodaje Score
			newApple();										// Tworzy nowe jablko
		}	
	}
	
	public void checkSuperApple()  
	{
		if((x[0] == superApplex) && (y[0] == superAppley))
		{
			bodyParts++;							// Dodaje partie weza
			applesEaten+=5;							// Dodaje Score
			newSuperApple();						// Tworzy nowe jablko
		}
	}
	
	public void checkCollisions() 				// Sprawdza kolizje pomiedzy scianami, czescia weza
	{
		for(int i = bodyParts; i > 0; i--)	
		{
			if((x[0] == x[i]) && (y[0] == y[i]))
			{	
				running = false;					// Przerywa gre jesli bedzie kolizja glowa - cialo
			}
		}
		
		if(x[0] < 0)				// Lewy sciana
		{
			running = false;
		}
		
		if(x[0] > SCREEN_WIDTH)		// Prawa sciana
		{
			running = false;
		}
		
		if(y[0] < 0)				// Dolna sciana
		{
			running = false;
		}
		
		if(y[0] > SCREEN_HEIGHT)	// Gorna sciana
		{
			running = false;
		}
		
		if(!running && !text)	
		{
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g)  	// Wypisuje napisy koncowe
	{
		g.setColor(Color.red);	
		g.setFont(new Font("Bebas", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!")) / 2, SCREEN_HEIGHT / 2); 
	
		g.setColor(Color.red);	
		g.setFont(new Font("Bebas", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());	
		
		g.setColor(Color.yellow);
		g.setFont(new Font("Sketch 3D",Font.PLAIN, 45));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Press Space to Restart", (SCREEN_WIDTH - metrics3.stringWidth("Press Space to Restart"))/2, 400);

	}
	
	@Override
	public void actionPerformed(ActionEvent e)	// Powtarza czynnosc, sprawdza caly czas
	{
		if(running)
		{
			text = false;		
			move();
			checkApple();
			checkSuperApple();
			checkCollisions();
		}
		repaint();
	}
	
	
	public class MyKeyAdapter extends KeyAdapter 	// Przypisanie klawiszy do poruszania sie weza
	{
		@Override
		public void keyPressed(KeyEvent e)	
		{
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_LEFT: if(direction != 'R') direction = 'L'; break;			// Nie mozna ruszac sie w prawo jesli waz rusza sie w lewo
				case KeyEvent.VK_RIGHT: if(direction != 'L') direction = 'R'; break;		// Nie mozna ruszac sie w lewo jesli waz rusza sie w prawo
				case KeyEvent.VK_UP: if(direction != 'D') direction = 'U'; break;			// Nie mozna ruszac sie w dol jesli waz rusza sie w gore
				case KeyEvent.VK_DOWN: if(direction != 'U') direction = 'D'; break;			// Nie mozna ruszac sie w gore jesli waz rusza sie w dol
				case KeyEvent.VK_P: if(GamePanel.gameOn) resume(); else pause(); break;		// Klawisz p - pause
			}
			
			if(!running && !text)						// Jesli zmienna running i text sa FALSE
			{
				if(e.getKeyChar() == KeyEvent.VK_SPACE)	// Przypisujemy spacji restart
				{
					bodyParts = 6;						// ustawiamy domyslne ustawienia dla weza
					direction = 'R';
					applesEaten = 0;
					for(int i = bodyParts; i > 0; i--)	// Resetujemy polozenie
					{
						x[i] = bodyParts * -1;
						y[i] = 0;
					}
					x[0] = 0;
					y[0] = 0;
					
					startGame();					// Zaczynamy od nowa jesli wcisniemy spacje
					repaint();
				}
			}
		}
	}
}
