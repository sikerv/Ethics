import java.text.AttributedString;
import java.util.Arrays;
import com.supermarket.*;

public class Agent extends SupermarketComponentImpl {

    public Agent() {
	super();
	shouldRunExecutionLoop = true;
	log.info("In Agent constructor.");
    }

    boolean firsttime = true;

	// aisle, shelf
	int goalLocation[] = {0, 0};
	int aisle = 0;
	int curr_item = 0;
	int list_quant[] = {2};


	// For testing purpose
	String shopping_list[] = {"brie cheese",};
	public void get_goalLocation (String shopping_list[], int curr_item) {
		// ADD REST
		int milk[] = {1, 0};
		int chocolate_milk[] = {1, 2};
		int strawberry_milk[] = {1, 4};
		int apples[] = {2, 5};
		int oranges[] = {2, 6};
		int banana[] = {2, 7};
		int strawberry[] = {2, 8};
		int raspberry[] = {2, 9};
		int sausage[] = {3, 10};
		int steak[] = {3, 11};
		int chicken[] = {3, 13};
		int ham[] = {3, 14};
		int brie_cheese[] = {4, 15};
		int swiss_cheese[] = {4, 16};
		int cheese_wheel[] = {4, 17};
		int garlic[] = {5, 19};
		int leek[] = {5, 20};
		int red_pepper[] = {5, 21};
		
		switch (shopping_list[curr_item]) {
			case "milk": goalLocation = milk;
				break;
			case "chocolate milk" : goalLocation = chocolate_milk;
				break;
			case "strawberry milk": goalLocation = strawberry_milk;
				break;
			case "apples": goalLocation = apples;
				break;
			case "strawberry": goalLocation = strawberry;
				break;
			case "brie cheese": goalLocation = brie_cheese;
				break;
			case "cheese wheel": goalLocation = cheese_wheel;
				break;
			case "steak": goalLocation = steak;
				break;
		}
	}


	// TODO: will always go to rear hub to reset, if in same aisle we want to go to shelf directly
	// TODO: doesn't pick up stawberry milk or last quantity of item
    @Override
    protected void executionLoop() {
	// this is called every 100ms
	// put your code in here
	Observation obs = getLastObservation();
		
		// Vaughan: method for getting the location of a shelf
	    	// can create another for loop which circulates through our custom array like Vincent suggested and stores locations
	    	// alternatively, can just use this to get the location on a constant basis as we cycle thru shopping list which is probably easier
	   	double targetx;
		double targety;
		Observation.Shelf target;
	    	for (int i = 0; i < obs.shelves.length; i++){
			if (obs.shelves[i].food.equals(goalLocation)){
				targetx = obs.shelves[i].position[0];
				targety = obs.shelves[i].position[1];
				target = obs.shelves[i];
			}
		}
	    	// Vaughan: method for figuring out collision range if we need to
	    	double targetcollideN = targety + (target.height / 2) + 1;
		double targetcollideS = targety - (target.height / 2) - 1;
		double targetcollideE = targetx - (target.width / 2) - .5;
		double targetcollideW = targetx + (target.width / 2) + .5;
	    
	    	// Vaughan: same for registers
// 	    	Observation.Register targetC = ob.registers[0];
// 	    	targetx = targetC.position[0];
// 		targety = targetC.position[1];
// 		double targetCCollideN = targety + .5;
// 		double targetCCollideS = targety - .5;
// 		double targetCCollideE = targetx + (targetC.width / 2) + .5;
// 		double targetCCollideW = targetx - (targetC.width / 2) - .5;
	    
	get_goalLocation(shopping_list, curr_item);
	aisle = goalLocation[0];
	boolean atAisle = (!obs.belowAisle(0, aisle) && !obs.aboveAisle(0, aisle));

	boolean atShelf = (!(obs.westOf(obs.players[0], obs.shelves[goalLocation[1]])) && !(obs.eastOf(obs.players[0], obs.shelves[goalLocation[1]])));

			// get the cart
			if ((obs.players[0].curr_cart == -1) && (obs.carts.length == 0)) {
				if (!obs.atCartReturn(0)) {
					goSouth();
				} else {
					interactWithObject();
				} 
			}


			

			// if (shopping_list[curr_item] == "strawberry milk" && atShelf) {
			// 	if ((obs.players[0].curr_cart == 0) || ((obs.players[0].curr_cart == -1) && (obs.players[0].holding_food != null))) {
			// 		toggleShoppingCart();
			// 	} else if (!obs.shelves[goalLocation[1]].canInteract(obs.players[0]) && (obs.players[0].curr_cart == -1) && (list_quant[curr_item] > 0) && (obs.players[0].holding_food == null)) {
			// 		goNorth(); 
			// 	} else if (obs.shelves[goalLocation[1]].canInteract(obs.players[0]) && (list_quant[curr_item] > 0)) {
			// 		interactWithObject();
			// 	}

			// 	if (!obs.carts[0].canInteract(obs.players[0]) && (obs.players[0].holding_food != null) && ((obs.players[0].curr_cart == -1))) {
			// 		// System.out.println("CODE BLOCK");
			// 		if (obs.westOf(obs.players[0], obs.carts[0])) {
			// 			goEast();
			// 		} else if (obs.eastOf(obs.players[0], obs.carts[0])) {
			// 			goWest();
			// 		}
					
			// 	// Drop item in cart
			// 	} else if (obs.carts[0].canInteract(obs.players[0]) && (obs.players[0].holding_food != null)) {
			// 		list_quant[curr_item]--; 
			// 		interactWithObject();
			// 	}
			
				
				// System.out.println("HOLDING? " + (obs.players[0].curr_cart != -1));
				// System.out.println("AT STRAWMILK SHELF? " + (atShelf));
			// }
			
			if ((obs.players[0].curr_cart == 0) && (list_quant[curr_item] > 0) &&  ((obs.inAisleHub(0)) || (obs.inRearAisleHub(0))) && (!atAisle)) {
				goToAisle(obs, goalLocation);
			} else if ((obs.players[0].curr_cart == 0) && (!obs.inAisleHub(0)) &&  (!obs.inAisle(0, aisle)) && (curr_item == 0)) {
				// System.out.println("GO EAST");
					if (!obs.inAisleHub(0) && !obs.inRearAisleHub(0)) {
						goEast();
					}
			} else if (((obs.players[0].curr_cart == 0)) && (list_quant[curr_item] == 0)) {
				// System.out.println("CODE BLOCK");
				if (!obs.inRearAisleHub(0)) {
					goEast();
				} else if (obs.inRearAisleHub(0)) {
					curr_item++;
				}
			}

			if (atAisle && !atShelf && list_quant[curr_item] > 0) {
				goToShelf(obs, goalLocation);
			}
	
			// Shop for item
			if (obs.inAisle(0, aisle) && list_quant[curr_item] > 0 && !atShelf) {
				goToShelf(obs, goalLocation);
			} else if (obs.inAisle(0, aisle) && atShelf) {

				
				// // Drop the cart to stop grabbing item
				if ((obs.players[0].curr_cart != -1) && (list_quant[curr_item] > 0)) {
				
					toggleShoppingCart();
				}
				
				if ((obs.players[0].curr_cart == -1) && list_quant[curr_item] >= 0) {
					
					// Get item from shelf
					if (!obs.shelves[goalLocation[1]].canInteract(obs.players[0]) && (list_quant[curr_item] > 0) && (obs.players[0].holding_food == null)) {
						goNorth();
						// System.out.println("PICKING UP: " + obs.shelves[goalLocation[1]].food);
						interactWithObject();
						list_quant[curr_item]--; 
					}

					// grabItem(obs);

					// Go to cart
					if (!obs.carts[0].canInteract(obs.players[0]) && (obs.players[0].holding_food != null) && ((obs.players[0].curr_cart == -1))) {
						// System.out.println("CODE BLOCK");
						if (obs.westOf(obs.players[0], obs.carts[0])) {
							goEast();
						} else if (obs.eastOf(obs.players[0], obs.carts[0])) {
							goWest();
						}
						
					// Drop item in cart
					} else if (obs.carts[0].canInteract(obs.players[0])) {
						// list_quant[curr_item]--; 
						interactWithObject();
					}

					// Toggle cart when we fufill quantity of that item
					if (((obs.players[0].curr_cart == -1)) && (list_quant[curr_item] == 0) && (obs.players[0].holding_food == null)) {
						toggleShoppingCart();
					}

				}
			}  
			

		
			if (((obs.players[0].curr_cart == 0)) && (list_quant[curr_item] == 0)) {
					// System.out.println("CODE BLOCK");
					if (!obs.inRearAisleHub(0)) {
						goEast();
					} else if (obs.inRearAisleHub(0)) {
						curr_item++;
					}
			}
		}

		// public void goBackToAisleHub (Observation obs) {
		// 	if ((obs.players[0].curr_cart == 0) && (!obs.inRearAisleHub(0))) {
		// 		goEast();
		// 	}
		// }

		public void goToAisle (Observation obs, int goalLocation[]) {
			//go to aisle and shelf
			aisle = goalLocation[0];
				if (obs.belowAisle(0, aisle)  ) {
					goNorth();
				} else if (obs.aboveAisle(0, aisle) ){
					goSouth();
				}
		}

		public void goToShelf (Observation obs, int goalLocation[]) {
			
			if (obs.westOf(obs.players[0], obs.shelves[goalLocation[1]])) {
				goEast();

			} else if (obs.eastOf(obs.players[0], obs.shelves[goalLocation[1]])) {
				goWest();
			}
		}

		public void grabItem (Observation obs) {
			if ((obs.players[0].curr_cart == -1) && (list_quant[curr_item] > 0) && (obs.players[0].holding_food == null)) {
				if (!obs.shelves[goalLocation[1]].canInteract(obs.players[0])) {
					goNorth();
				// } else if (obs.shelves[goalLocation[1]].canInteract(obs.players[0])) {
					interactWithObject();
					list_quant[curr_item]--;
				}
			}
		}
	}

