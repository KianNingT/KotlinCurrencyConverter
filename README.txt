This project is a simple currency converter consisting several libraries such as dagger 
hilt, state flow, view model, retrofit, view binding, coroutines together with the 
MVVM architectural design pattern. The free API that is being used in this project is 
from ExchangeRate-API and the base URL is https://www.exchangerate-api.com/.
The project is made up of 1 activity only because there wouldn't be a need to navigate
to other pages for converting currencies.

Mvvm is used to separate the UI, the business logic and the data source so the code
will be more future proof by enabling others to easily understand the code structure,
extend the project with more features and for covinience of testing. In this project,
the M(Model) represents the data source, which is also represented by a repository
that collects all data in one central place that is data from a remote API. 
The VM(ViewModel) contains the business logic and other functions. In this project, the
ViewModel is responsible for calculating the chosen paired rates, converting the rates
with an given amount, getting the corresponding chosen rates, and also handling the 
error whenever any of the mentioned functions fail to return a result. The ViewModel in
this project is in direct communication with the Model to get access to the data it 
needs. 
The V(View)/ UI Controller is the MainActivity of the project. It contains all UI 
elements and gets events from our ViewModel to determine when it should update 
its views. In the context of the project, the View of the project will either display 
the converted currencies after successfully calling the API or relay the failure 
message when encountering some exceptions. No business logic is being written in the 
View and only logic to directly manipulate the views. Using this architectural design
pattern will enable the project to be highly scalable, understandable and testable

I have decided to use StateFlow instead of LiveData due to the fact that StateFlow is
entirely based on Coroutine and can use Flow operators which is something 
LiveData can't achieve. StateFlow also requires an initial value which cannot be null
at any time. This decision does not mean that LiveData is inferior, it is
just a preference of mine for this particular project.

The project's main repository is an interface because the ViewModel needs an instance
of the repository to make the actual network request. But we wouldn't want to make 
network request when writing unit tests or normal test cases because tests should
be quick in general. So the approached i took enables test cases to be written
in another version of a repository aka a special repository that just simulates the 
bahavior of the API but it doesn't make any actual network request. Therefore, I think
that it is helpful to have the interface and also multiple versions of repository.
A default repository that makes the network request, and special repositories during
testing and both inherits from this main repository interface.
