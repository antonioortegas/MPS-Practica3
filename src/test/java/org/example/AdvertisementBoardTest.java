package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdvertisementBoardTest {

    Advertisement advertisement;
    AdvertisementBoard board;
    AdvertiserDatabase db;
    PaymentGateway gateway;

    @BeforeEach
    void setUp() {
        board = new AdvertisementBoard();
        db = Mockito.mock(AdvertiserDatabase.class);
        gateway = Mockito.mock(PaymentGateway.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Check there is initially and ad on the board when creating a new one")
    void shouldNumberOfPublishedAdvertisementsReturnOneWhenCreatingANewBoard(){

        int expectedValue = 1;

        int actualValue = board.numberOfPublishedAdvertisements();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    @DisplayName("Number of advertisements should increase by one when a new ad is created")
    void shouldNumberOfAdvertisementsIncreaseByOneWhenCreatingANewAd(){
        int expectedValue = 2;
        advertisement = new Advertisement("Title", "text", "THE Company");

        board.publish(advertisement, db, gateway);
        int actualValue = board.numberOfPublishedAdvertisements();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    @DisplayName("Publish ad from 'Pepe Gotera y Otilio' only if they have enough funds")
    void shouldNumberOfAdvertisementsStayTheSameWhenTryingCreatingANewAddWithoutFunds(){
        int expectedValue = 1;
        advertisement = new Advertisement("Title", "text", "Pepe Gotera y Otilio");
        Mockito.when(db.advertiserIsRegistered("Pepe Gotera y Otilio")).thenReturn(true); //Advertiser is registered on database
        Mockito.when(gateway.advertiserHasFunds("Pepe Gotera y Otilio")).thenReturn(false); //Simulate advertiser does NOT have enough funds

        board.publish(advertisement, db, gateway);
        int actualValue = board.numberOfPublishedAdvertisements();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    @DisplayName("Publish ad from 'Robin Robot' correctly")
    void shouldChargeAdvertiserFunctionIsCalledOnceWhenCorrectlyCreatingANewAd(){
        advertisement = new Advertisement("Title", "text", "Robin Robot");
        Mockito.when(db.advertiserIsRegistered("Robin Robot")).thenReturn(true);
        Mockito.when(gateway.advertiserHasFunds("Robin Robot")).thenReturn(true);

        board.publish(advertisement, db, gateway);

        verify(gateway, times(1)).chargeAdvertiser("Robin Robot");
    }

    @Test
    @DisplayName("Create 2 new ads from THE Company and check that one of them is not on the board after deleting it")
    void shouldFindByTitleReturnEmptyIfTheAdvertisementIsNotOnTheBoard(){
        advertisement = new Advertisement("AdOne", "text", "THE Company");
        board.publish(advertisement, db, gateway);
        advertisement = new Advertisement("AdTwo", "text", "THE Company");
        board.publish(advertisement, db, gateway);
        Optional<Advertisement> expectedAdvertisement = Optional.empty(); //Expect empty ad

        board.deleteAdvertisement("AdOne", "THE Company"); //Delete First One
        Optional<Advertisement> actualAdvertisement = board.findByTitle("AdOne");   //Try to find ad "AdOne"

        assertEquals(expectedAdvertisement, actualAdvertisement);
    }
}

/*
    TEMPLATE
    @Test
        @DisplayName("")
        void (){

        }
 */