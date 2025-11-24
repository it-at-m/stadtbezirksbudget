import type { DefaultTheme } from "vitepress";

import pages from "./pages";

const navigation: DefaultTheme.NavItem[] = [
  { text: "Home", link: "/" },
  ...pages,
];

export default navigation;
