import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IArea } from 'app/shared/model/area.model';
import { getEntities as getAreas } from 'app/entities/area/area.reducer';
import { getEntity, updateEntity, createEntity, reset } from './customer.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICustomerUpdateState {
  isNew: boolean;
  areaId: string;
}

export class CustomerUpdate extends React.Component<ICustomerUpdateProps, ICustomerUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      areaId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getAreas();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { customerEntity } = this.props;
      const entity = {
        ...customerEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/customer');
  };

  render() {
    const { customerEntity, areas, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="abstraktApp.customer.home.createOrEditLabel">
              <Translate contentKey="abstraktApp.customer.home.createOrEditLabel">Create or edit a Customer</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : customerEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="customer-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="abstraktApp.customer.name">Name</Translate>
                  </Label>
                  <AvField
                    id="customer-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    <Translate contentKey="abstraktApp.customer.email">Email</Translate>
                  </Label>
                  <AvField id="customer-email" type="text" name="email" />
                </AvGroup>
                <AvGroup>
                  <Label id="phoneLabel" for="phone">
                    <Translate contentKey="abstraktApp.customer.phone">Phone</Translate>
                  </Label>
                  <AvField id="customer-phone" type="text" name="phone" />
                </AvGroup>
                <AvGroup>
                  <Label id="frequencyLabel">
                    <Translate contentKey="abstraktApp.customer.frequency">Frequency</Translate>
                  </Label>
                  <AvInput
                    id="customer-frequency"
                    type="select"
                    className="form-control"
                    name="frequency"
                    value={(!isNew && customerEntity.frequency) || 'MONTHLY'}
                  >
                    <option value="MONTHLY">
                      <Translate contentKey="abstraktApp.Frequency.MONTHLY" />
                    </option>
                    <option value="TWO_MONTHLY">
                      <Translate contentKey="abstraktApp.Frequency.TWO_MONTHLY" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="paymentMethodLabel">
                    <Translate contentKey="abstraktApp.customer.paymentMethod">Payment Method</Translate>
                  </Label>
                  <AvInput
                    id="customer-paymentMethod"
                    type="select"
                    className="form-control"
                    name="paymentMethod"
                    value={(!isNew && customerEntity.paymentMethod) || 'BANK'}
                  >
                    <option value="BANK">
                      <Translate contentKey="abstraktApp.PaymentMethod.BANK" />
                    </option>
                    <option value="CASH">
                      <Translate contentKey="abstraktApp.PaymentMethod.CASH" />
                    </option>
                    <option value="DIRECT_DEBIT">
                      <Translate contentKey="abstraktApp.PaymentMethod.DIRECT_DEBIT" />
                    </option>
                    <option value="ONLINE">
                      <Translate contentKey="abstraktApp.PaymentMethod.ONLINE" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="flatPositionLabel" for="flatPosition">
                    <Translate contentKey="abstraktApp.customer.flatPosition">Flat Position</Translate>
                  </Label>
                  <AvField id="customer-flatPosition" type="text" name="flatPosition" />
                </AvGroup>
                <AvGroup>
                  <Label id="numberLabel" for="number">
                    <Translate contentKey="abstraktApp.customer.number">Number</Translate>
                  </Label>
                  <AvField
                    id="customer-number"
                    type="text"
                    name="number"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="streetLabel" for="street">
                    <Translate contentKey="abstraktApp.customer.street">Street</Translate>
                  </Label>
                  <AvField
                    id="customer-street"
                    type="text"
                    name="street"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="area.name">
                    <Translate contentKey="abstraktApp.customer.area">Area</Translate>
                  </Label>
                  <AvInput id="customer-area" type="select" className="form-control" name="area.id">
                    <option value="" key="0" />
                    {areas
                      ? areas.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/customer" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  areas: storeState.area.entities,
  customerEntity: storeState.customer.entity,
  loading: storeState.customer.loading,
  updating: storeState.customer.updating
});

const mapDispatchToProps = {
  getAreas,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerUpdate);
