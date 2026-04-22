var modal = document.getElementById("viewImageModal");
var modalImg = document.getElementById("modalImage");
var captionText = document.getElementById("caption");
document.querySelectorAll(".view-image-btn").forEach((button) => {
	button.onclick = function () {
		modalImg.classList.remove("fade-out");
		modal.classList.remove("fade-out-parent");
		modal.classList.add("fade-in-parent");
		modalImg.classList.add("fade-in");
		setTimeout(() => {
			modal.classList.remove("hidden");
			modal.classList.add("flex");
		}, 250);
		modalImg.src = this.getAttribute("data-image-src");
		captionText.innerHTML = this.getAttribute("data-image-alt");
	};
});

document.querySelector(".close").onclick = function () {
	modalImg.classList.remove("fade-in");
	modal.classList.remove("fade-in-parent");
	modalImg.classList.add("fade-out");
	modal.classList.add("fade-out-parent");
	setTimeout(() => {
		modal.classList.add("hidden");
	}, 250);
};
