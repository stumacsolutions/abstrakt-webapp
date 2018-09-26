import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IArea, defaultValue } from 'app/shared/model/area.model';

export const ACTION_TYPES = {
  FETCH_AREA_LIST: 'area/FETCH_AREA_LIST',
  FETCH_AREA: 'area/FETCH_AREA',
  CREATE_AREA: 'area/CREATE_AREA',
  UPDATE_AREA: 'area/UPDATE_AREA',
  DELETE_AREA: 'area/DELETE_AREA',
  RESET: 'area/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IArea>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AreaState = Readonly<typeof initialState>;

// Reducer

export default (state: AreaState = initialState, action): AreaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AREA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AREA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AREA):
    case REQUEST(ACTION_TYPES.UPDATE_AREA):
    case REQUEST(ACTION_TYPES.DELETE_AREA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AREA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AREA):
    case FAILURE(ACTION_TYPES.CREATE_AREA):
    case FAILURE(ACTION_TYPES.UPDATE_AREA):
    case FAILURE(ACTION_TYPES.DELETE_AREA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AREA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AREA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AREA):
    case SUCCESS(ACTION_TYPES.UPDATE_AREA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AREA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/areas';

// Actions

export const getEntities: ICrudGetAllAction<IArea> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_AREA_LIST,
  payload: axios.get<IArea>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IArea> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AREA,
    payload: axios.get<IArea>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IArea> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AREA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IArea> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AREA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IArea> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AREA,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
