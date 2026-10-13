
async function advanceDeliveryStatus() {
    const input = document.getElementById("trackingOrderId");

    if (!currentTrackingOrderId && input) {
        currentTrackingOrderId = input.value.trim();
    }

    if (!currentTrackingOrderId) {
        alert("Track an order first.");
        return;
    }

    try {
        const response = await fetch(
            `${API_BASE_URL}/api/orders/${encodeURIComponent(currentTrackingOrderId)}/tracking/advance`,
            {
                method: "POST"
            }
        );

        await readResponse(response);

        if (input) {
            input.value = currentTrackingOrderId;
        }

        await trackOrder();

    } catch (error) {
        console.error("Delivery status update error:", error);
        alert(`Could not update delivery status: ${error.message}`);
    }
}

                            
