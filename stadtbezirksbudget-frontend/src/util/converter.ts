/**
 * Converts an object to URLSearchParams.
 * - Ignores undefined, null, and empty string values.
 * - Joins array values with commas.
 * - Does not handle nested objects.
 * @param object - The object to convert
 * @param params - Optional existing URLSearchParams to append to
 * @returns The resulting URLSearchParams
 */
export function objectToSearchParams(
  object: object,
  params: URLSearchParams = new URLSearchParams()
): URLSearchParams {
  Object.entries(object).forEach(([key, value]) => {
    if (value === undefined || value === null || value === "") return;

    if (Array.isArray(value)) {
      if (value.length === 0) return;
      params.append(key, value.join(","));
    } else {
      params.append(key, String(value));
    }
  });
  return params;
}

/**
 * Converts a route parameter to a string.
 *
 * This function takes a route parameter which can be a string, an array of strings,
 * or undefined. It returns the first string from the array, or the string itself,
 * or an empty string if the parameter is undefined.
 *
 * @param {string | string[] | undefined} param - The route parameter to convert.
 * @returns {string} The first string from the array, the string itself, or an empty string.
 */
export function routeParamsToString(
  param: string | string[] | undefined
): string {
  return Array.isArray(param) ? (param[0] ?? "") : (param ?? "");
}
