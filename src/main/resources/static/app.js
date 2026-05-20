const BASE_URL = " https://kaim-kaar.onrender.com";

function logout(){

    localStorage.clear();

    window.location.href = "login.html";
}

async function registerUser(){

    const userName =
        document.getElementById("userName").value;

    const email =
        document.getElementById("email").value;

    const password =
        document.getElementById("password").value;

    const role =
        document.getElementById("role").value;

    const response = await fetch(`${BASE_URL}/auth/register`,{

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify({
            userName,
            email,
            password,
            role
        })

    });

    if(response.ok){

        alert("Registration Successful");

        window.location.href = "login.html";
    }
    else{
        alert("Registration Failed");
    }
}

async function loginUser(){

    const email =
        document.getElementById("loginEmail").value;

    const password =
        document.getElementById("loginPassword").value;

    const response = await fetch(`${BASE_URL}/auth/login`,{

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify({
            email,
            password
        })

    });

    const data = await response.json();

    localStorage.setItem("token",data.token);
    localStorage.setItem("role",data.role);
    localStorage.setItem("userId", data.userId);

    if(data.role === "PROVIDER"){

        window.location.href = "provider.html";
    }
    else{

        window.location.href = "customer.html";
    }
}

async function addService() {

    const token = localStorage.getItem("token");

    const providerId = localStorage.getItem("userId");

    const description =
        document.getElementById("description").value;

    const price =
        document.getElementById("price").value;

    const location =
        document.getElementById("location").value;

    const categoryId =
        document.getElementById("categoryId").value;

    const response = await fetch(
        "http://localhost:8080/provider/service",
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },

            body: JSON.stringify({

                description: description,

                price: price,

                location: location,

                provider: {
                    id: providerId
                },

                serviceCategory: {
                    id: categoryId
                }

            })
        }
    );

    if(response.ok){
        alert("Service Added Successfully");
        loadMyServices();
    }else{
        alert("Failed To Add Service");
    }
}

async function loadProviders(){

    const token = localStorage.getItem("token");

    const response = await fetch(`${BASE_URL}/provider/services`,{

        headers:{
            "Authorization":"Bearer " + token
        }

    });

    const providers = await response.json();

    const container =
        document.getElementById("providerContainer");

    if(providers.length === 0){

        container.innerHTML = `
            <div class="empty">
                No providers available
            </div>
        `;

        return;
    }

    container.innerHTML = "";

    providers.forEach(provider => {

        container.innerHTML += `

            <div class="card">

                <h3>
                    ${provider.serviceCategory.name}
                </h3>

                <p>
                    ${provider.description}
                </p>

                <p>
                    📍 ${provider.location}
                </p>

                <p>
                    💰 ₹${provider.price}
                </p>

                <p>
                    👨‍🔧 ${provider.provider.userName}
                </p>

                <input type="text"
                placeholder="Enter Address"
                id="address${provider.id}">

                <input type="number"
                placeholder="Phone Number"
                id="phone${provider.id}">

                <button onclick="bookService(${provider.id})">
                    Book Now
                </button>

            </div>

        `;
    });

}

async function bookService(providerId){

    const token = localStorage.getItem("token");

    const customerId = localStorage.getItem("userId");

    const address =
        document.getElementById("address" + providerId).value;

    const phone =
        document.getElementById("phone" + providerId).value;

    const response = await fetch(`${BASE_URL}/booking`,{

        method:"POST",

        headers:{
            "Content-Type":"application/json",
            "Authorization":"Bearer " + token
        },

        body:JSON.stringify({

            customer:{
                id:customerId
            },

            providerCategory:{
                id:providerId
            },

            address,
            phoneNumber:phone
        })

    });

    if(response.ok){

        alert("Booking Sent");
    }
    else{

        alert("Booking Failed");
    }
}
async function loadBookings(){

    const token = localStorage.getItem("token");

    const response = await fetch(`${BASE_URL}/booking`,{

        headers:{
            "Authorization":"Bearer " + token
        }

    });

    const bookings = await response.json();

    const container =
        document.getElementById("bookingContainer");

    if(bookings.length === 0){

        container.innerHTML = `
            <div class="empty">
                No bookings yet 🚀
            </div>
        `;

        return;
    }

    container.innerHTML = "";

    bookings.forEach(booking => {

        container.innerHTML += `

            <div class="card">

                <h3>
                    ${booking.serviceName}
                </h3>

                <p>
                    👤 ${booking.customerName}
                </p>

                <p>
                    📍 ${booking.address}
                </p>

                <p>
                    📞 ${booking.phoneNumber}
                </p>

                <p>
                    💰 ₹${booking.price}
                </p>

                <div class="status ${booking.status.toLowerCase()}">
                    ${booking.status}
                </div>

                <div class="flex">

                    <button onclick="acceptBooking(${booking.bookingId})">
                        Accept
                    </button>

                    <button class="logout"
                    onclick="rejectBooking(${booking.bookingId})">
                        Reject
                    </button>

                </div>

            </div>

        `;
    });

}
async function loadCategories(){

    const response =
        await fetch(`${BASE_URL}/category`);

    const categories =
        await response.json();

    const select =
        document.getElementById("categoryId");

    select.innerHTML = "";

    categories.forEach(category => {

        select.innerHTML += `

            <option value="${category.id}">
                ${category.name}
            </option>

        `;
    });

}

async function acceptBooking(id){

    const token = localStorage.getItem("token");

    await fetch(`${BASE_URL}/booking/accept/${id}`,{

        method:"PUT",

        headers:{
            "Authorization":"Bearer " + token
        }

    });

    loadBookings();
}

async function rejectBooking(id){

    const token = localStorage.getItem("token");

    await fetch(`${BASE_URL}/booking/reject/${id}`,{

        method:"PUT",

        headers:{
            "Authorization":"Bearer " + token
        }

    });

    loadBookings();
}

if(window.location.pathname.includes("customer.html")){

    loadProviders();
}

if(window.location.pathname.includes("provider.html")){

    loadBookings();

    loadCategories();
}