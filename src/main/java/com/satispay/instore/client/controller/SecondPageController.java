package com.satispay.instore.client.controller;

import com.satispay.instore.client.client_needs_to_implement_this_classes.PersistenceProtoCoreClientImpl;
import com.satispay.protocore.active.PersistenceProtoCore;
import com.satispay.protocore.active.ProtoCoreHttpClientProvider;
import com.satispay.protocore.models.transactions.CloseTransaction;
import com.satispay.protocore.models.transactions.TransactionProposal;
import com.satispay.protocore.persistence.MemoryPersistenceManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import okhttp3.Request;
import okhttp3.Response;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * --> Created by domenicovisconti on 19/09/16.
 */
public class SecondPageController implements Initializable {

    public TextField profileMeInfo;
    public ImageView profileImage;
    public Button retrieveInfoButton;
    public TableView<TransactionProposal> transactionsTable;
    public Button pendingTransactions;
    public Button acceptButton;
    public Button refuseButton;

    private PersistenceProtoCore persistenceProtoCore;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initRetrieveInfoButton();
        initAcceptButton();
        initRefuseButton();

        persistenceProtoCore = PersistenceProtoCoreClientImpl.getInstance();
    }

    private void initTable() {
        TableColumn<TransactionProposal, String> columnName = new TableColumn<>("Name");
        columnName.setCellValueFactory(new PropertyValueFactory<>("consumer.name"));
        columnName.setMinWidth(100D);

        TableColumn<TransactionProposal, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setMinWidth(100D);

        TableColumn<TransactionProposal, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dateColumn.setMinWidth(300D);

        TableColumn<TransactionProposal, String> uidColumn = new TableColumn<>("ID");
        uidColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        uidColumn.setMinWidth(100D);

        transactionsTable.getColumns().addAll(Arrays.asList(columnName, amountColumn, dateColumn, uidColumn));
    }

    private void initRetrieveInfoButton() {
        retrieveInfoButton.setOnAction(event -> {

            // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
            // here a re some reference:
            //  - http://reactivex.io
            //  - https://github.com/ReactiveX/RxJava
            persistenceProtoCore
                    .profileMe()
                    .take(1)
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(
                            profileMe -> Platform.runLater(() -> {
                                profileMeInfo.setText(
                                        profileMe.getShop().getName() + profileMe.getShop().getPhoneNumber() +
                                                profileMe.getShop().getAddress().getAddress()
                                );

                                try {
                                    Request request = new Request.Builder().url(new URL(profileMe.getShop().getImageUrl())).build();
                                    Response response = ProtoCoreHttpClientProvider.getInstance().getProtocoreClientNoSignatureVerify(false, MemoryPersistenceManager.getInstance()).newCall(request).execute();

                                    profileImage.setImage(new Image(response.body().byteStream(), 70, 70, false, false));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            })
                    );
        });

        pendingTransactions.setOnAction(event -> {
            Platform.runLater(() -> {
                transactionsTable.getItems().clear();
                transactionsTable.refresh();
            });

            // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
            // here a re some reference:
            //  - http://reactivex.io
            //  - https://github.com/ReactiveX/RxJava
            persistenceProtoCore
                    .getTransactionHistory(20, null, "proposed")
                    .subscribeOn(Schedulers.newThread())
                    .take(1)
                    .subscribe(historyTransactionsModel ->
                            Platform.runLater(() -> {
                                        transactionsTable.setItems(
                                                FXCollections.observableList(historyTransactionsModel.getList())
                                        );
                                        transactionsTable.refresh();
                                    }
                            )
                    );
        });
    }

    private void initAcceptButton() {
        acceptButton.setOnAction(event -> {
            TransactionProposal selectedItem = transactionsTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {

                // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
                // here a re some reference:
                //  - http://reactivex.io
                //  - https://github.com/ReactiveX/RxJava
                persistenceProtoCore
                        .closeTransaction(new CloseTransaction(TransactionProposal.TransactionState.APPROVED.getRawValue()), selectedItem.getTransactionId())
                        .take(1)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(
                                transactionProposal -> {
                                    transactionsTable.getItems().clear();
                                    transactionsTable.refresh();
                                },
                                throwable -> {
                                }
                        );
            }
        });
    }

    private void initRefuseButton() {
        refuseButton.setOnAction(event -> {
            TransactionProposal selectedItem = transactionsTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {

                // ==> Here is how the api in store requests are invoked. The Rx Observable pattern is used.
                // here a re some reference:
                //  - http://reactivex.io
                //  - https://github.com/ReactiveX/RxJava
                persistenceProtoCore
                        .closeTransaction(new CloseTransaction(TransactionProposal.TransactionState.CANCELED.getRawValue()), selectedItem.getTransactionId())
                        .take(1)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(
                                transactionProposal -> {
                                    transactionsTable.getItems().clear();
                                    transactionsTable.refresh();
                                },
                                throwable -> {
                                }
                        );
            }
        });
    }

}
