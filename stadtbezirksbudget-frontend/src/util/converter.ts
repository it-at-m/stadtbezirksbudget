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
