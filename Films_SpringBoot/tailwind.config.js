/** @type {import('tailwindcss').Config} */
module.exports = {
	content: ["src/main/resources/templates/**/*.html"],
	theme: {
		extend: {
			colors: {
				midnight: "rgb(2, 0, 36)", // A deep, dark shade resembling the night sky.
				twilight: "rgb(3, 3, 56)", // A slightly brighter tone evoking the twilight hour.
				oceanBlue: "rgb(30, 58, 138)", // A rich, deep blue reminiscent of the ocean.
			},
		},
	},
	plugins: [],
};
