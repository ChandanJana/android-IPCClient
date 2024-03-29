# android-IPCClient
Follw the 
@Link 
https://proandroiddev.com/ipc-techniques-for-android-45d815ac59be

IPC is a general concept that means inter-process communication.

When it comes to Android, IPC covers the following two situations:

Communication between applications
Communication of processes in a multi-process application (Application whose components such as Activity, Service, Receiver, Provider are run in different processes)
In this article, we will create examples of inter-application communication.

There are 3 basic methods used for IPC on Android:

AIDL
Messenger
Broadcast
It is also possible to use traditional Linux techniques (such as socket communication, shared files) to perform IPC. However, official documents recommend choosing one of the three methods specific to Android, which we mentioned above, in terms of security. Advantages such as authenticating the application that wants to communicate are possible in these three methods.

In this article series, we will look into the details of all 3 techniques and how to implement them.
